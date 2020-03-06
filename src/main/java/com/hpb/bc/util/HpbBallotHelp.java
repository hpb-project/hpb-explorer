/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hpb.bc.async.AsyncTask;
import com.hpb.bc.configure.HpbBallotProperties;
import com.hpb.bc.contracts.FechHpbBallotAddr;
import com.hpb.bc.contracts.HpbBallotVote;
import com.hpb.bc.contracts.Hpbballot;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.DynamicArray;
import io.hpb.web3.abi.datatypes.generated.Bytes32;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.crypto.WalletUtils;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbBlockNumber;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.http.HttpService;
import io.hpb.web3.protocol.ipc.UnixIpcService;
import io.hpb.web3.protocol.ipc.WindowsIpcService;
import io.hpb.web3.tuples.generated.Tuple2;
import io.hpb.web3.tuples.generated.Tuple3;
import io.hpb.web3.tuples.generated.Tuple4;
import io.hpb.web3.tx.ChainId;
import io.hpb.web3.tx.RawTransactionManager;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;

@Component
public class HpbBallotHelp {
    private static final int SCALE = 500;
    private static final int SCALE1 = 320;

    public Logger log = LoggerFactory.getLogger(HpbBallotHelp.class);

    private static final BigInteger MIN_BALANCE = Convert.toWei("1", Convert.Unit.SZABO).toBigInteger();
    private static final BigInteger GAS_PRICE = Convert.toWei("18", Convert.Unit.GWEI).toBigInteger();
    private static final BigInteger GAS_LIMIT = new BigInteger("100000000");
    private static final String SALT = "zmqa9QHTeIjlSwHLbYxN5Unc4ajPEvyu";
    private final static long COUNTDOWNLATCH_AWAIT_MINUTES = 5;

    @Autowired
    private Admin admin;
    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private HpbBallotProperties hpbBallotProperties;


    private static final ThreadLocal<Hpbballot> hpbballotHolder = new ThreadLocal<Hpbballot>();
    private static final ThreadLocal<Credentials> credentialsHolder = new ThreadLocal<Credentials>();
    private static final ThreadLocal<RawTransactionManager> transactionManagerHolder =
            new ThreadLocal<RawTransactionManager>();
    private static final ThreadLocal<Credentials> credentialsPassHolder = new ThreadLocal<Credentials>();
    private static final ThreadLocal<RawTransactionManager> transactionPassManagerHolder =
            new ThreadLocal<RawTransactionManager>();

    private FechHpbBallotAddr fechHpbBallotAddr = null;

    private Credentials getCredentials() throws Exception {
        Credentials credentials = credentialsHolder.get();
        if (credentials == null) {
            String password = SecurityUtils.decodeAESBySalt(hpbBallotProperties.getPassword(), SALT);
            String keyPath = hpbBallotProperties.getKeyPath();
            credentials = WalletUtils.loadCredentials(password, keyPath);
            credentialsHolder.set(credentials);
            log.info("setCredentials[{}]", credentials.hashCode());
        }
        return credentials;
    }

    private RawTransactionManager getTransactionManager() throws Exception {
        RawTransactionManager transactionManager = transactionManagerHolder.get();
        if (transactionManager == null) {
            transactionManager = new RawTransactionManager(admin, getCredentials(), ChainId.MAINNET);
            transactionManagerHolder.set(transactionManager);
            log.info("setTransactionManager[{}]", transactionManager.hashCode());
        }
        return transactionManager;
    }

    private Credentials getCredentials(String password) throws Exception {
        if (StringUtils.isBlank(password)) {
            password = SecurityUtils.decodeAESBySalt(hpbBallotProperties.getPassword(), SALT);
        }
        Credentials credentials = credentialsPassHolder.get();
        if (credentials == null) {
            String keyPath = hpbBallotProperties.getKeyPath();
            credentials = WalletUtils.loadCredentials(password, keyPath);
            credentialsPassHolder.set(credentials);
        }
        return credentials;
    }

    private RawTransactionManager getTransactionManager(String password, Admin newAdmin) throws Exception {
        if (StringUtils.isBlank(password)) {
            password = SecurityUtils.decodeAESBySalt(hpbBallotProperties.getPassword(), SALT);
        }
        RawTransactionManager transactionManager = transactionPassManagerHolder.get();
        if (transactionManager == null) {
            transactionManager = new RawTransactionManager(newAdmin, getCredentials(password), ChainId.MAINNET);
            transactionPassManagerHolder.set(transactionManager);
        }
        return transactionManager;
    }

    public Hpbballot getHpbballot() throws Exception {
        Hpbballot hpbballot = hpbballotHolder.get();
        if (hpbballot == null) {
            hpbballot = Hpbballot.load(hpbBallotProperties.getHpbBallotContractAddress(), admin,
                    getTransactionManager(), GAS_PRICE, GAS_LIMIT);
            hpbballotHolder.set(hpbballot);
            log.info("setHpbballot[{}]", hpbballot.hashCode());
        }
        return hpbballot;
    }

    private FechHpbBallotAddr getFechHpbBallotAddr() throws Exception {
        if (fechHpbBallotAddr == null) {
            fechHpbBallotAddr = FechHpbBallotAddr.load(hpbBallotProperties.getInnerContractAddress(), admin,
                    getTransactionManager(), GAS_PRICE, GAS_LIMIT);
        }
        return fechHpbBallotAddr;
    }


    private boolean checkSucess(String status) {
        if (StringUtils.isBlank(status)) {
            return false;
        }
        BigInteger decodeQuantity = Numeric.decodeQuantity(status);
        if (decodeQuantity == null) {
            return false;
        }
        return decodeQuantity.intValue() == 1;
    }

    private Map<String, BigInteger> getVoterSnapshotBalance(List<Address> addrs) throws Exception {
        Map<String, BigInteger> snapshotBalance = new ConcurrentHashMap<String, BigInteger>();
        if (CollectionUtils.isEmpty(addrs)) {
            return snapshotBalance;
        }
        BigInteger _currentSnapshotBlock = getHpbballot().currentSnapshotBlock().send().getValue();
        Instant start = Instant.now();
        int size = addrs.size();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (Address addr : addrs) {
            if (snapshotBalance.get(addr.getValue()) != null) {
                countDownLatch.countDown();
            } else {
                asyncTask.getVoterSnapshotBalanceFromNode(countDownLatch, addr,
                        snapshotBalance, _currentSnapshotBlock);
            }
        }
        countDownLatch.await(COUNTDOWNLATCH_AWAIT_MINUTES, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个地址快照{}余额信息,花费时间：{}",
                snapshotBalance.size(), _currentSnapshotBlock,
                DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        if (snapshotBalance.size() != size) {
            throw new Exception("从区块链节点上获取快照余额信息失败");
        }
        return snapshotBalance;
    }

    private void setSnapshotBalanceBatch(Map<String, BigInteger> voterSnapshotBalance) throws Exception {
        if (MapUtils.isEmpty(voterSnapshotBalance)) {
            return;
        }
        Hpbballot hpbballot = getHpbballot();
        List<Uint256> voterSnapshot = new LinkedList<Uint256>();
        List<Address> newAddrs = new LinkedList<Address>();
        int setSnapshotBalanceNum = 0;
        for (Map.Entry<String, BigInteger> en : voterSnapshotBalance.entrySet()) {
            BigInteger bigBalance = en.getValue();
            voterSnapshot.add(new Uint256(bigBalance));
            newAddrs.add(new Address(en.getKey()));
            if (newAddrs.size() > SCALE) {
                DynamicArray<Uint256> _snapshotBalances = new DynamicArray<Uint256>(voterSnapshot);
                DynamicArray<Address> _voterAddrs = new DynamicArray<Address>(newAddrs);
                TransactionReceipt send = hpbballot.setSnapshotBalanceBatch(_voterAddrs, _snapshotBalances).send();
                log.info(send.getLogs().toString());
                setSnapshotBalanceNum++;
                log.info("{}", setSnapshotBalanceNum);
                newAddrs = null;
                voterSnapshot = null;
                voterSnapshot = new LinkedList<Uint256>();
                newAddrs = new LinkedList<Address>();
                if (!checkSucess(send.getStatus())) {
                    throw new Exception("设置快照失败");
                }
            }
        }
        if (newAddrs.size() > 0) {
            DynamicArray<Uint256> _snapshotBalances = new DynamicArray<Uint256>(voterSnapshot);
            DynamicArray<Address> _voterAddrs = new DynamicArray<Address>(newAddrs);
            TransactionReceipt send = hpbballot.setSnapshotBalanceBatch(_voterAddrs, _snapshotBalances).send();
            log.info(send.getLogs().toString());
            setSnapshotBalanceNum++;
            log.info("{}", setSnapshotBalanceNum);
        }
    }

    /**
     * @return 组装需要代投票的数据
     * @throws Exception
     */
    private List<HpbBallotVote> getBatchVote() throws Exception {
        Map<String, BigDecimal> voterPercent = getVoterPercent();
        Hpbballot hpbballot = getHpbballot();
        BigInteger currentIndex = hpbballot.fechCurrentSnapshotBlockIndex().send().getValue();
        BigInteger preIndex = currentIndex.subtract(BigInteger.ONE);
        // 前一个快照号
        Uint256 _preSnapshotBlock = hpbballot.fechSnapshotBlockByIndex(new Uint256(preIndex)).send();
        // 当前快照号
        Uint256 _currentSnapshotBlock = hpbballot.currentSnapshotBlock().send();
        List<HpbBallotVote> list = new ArrayList<HpbBallotVote>();
        Tuple2<DynamicArray<Address>, DynamicArray<Uint256>> send3 = hpbballot
                .fechAllVoteResultBySnapshotBlock(_currentSnapshotBlock).send();
        List<Address> oldAddrs = send3.getValue1().getValue();
        List<Uint256> oldNums = send3.getValue2().getValue();
        List<Address> addrs = new LinkedList<Address>();
        //去除候选人投票为零的数据
        for (int i = 0; i < oldAddrs.size(); i++) {
            if (oldNums.get(i) != null) {
                if (!BigInteger.ZERO.equals(oldNums.get(i).getValue())) {
                    addrs.add(oldAddrs.get(i));
                }
            }
        }
        for (Address addr : addrs) {
            Tuple2<DynamicArray<Address>, DynamicArray<Uint256>> send2 = hpbballot
                    .fechVoteResultForCandidateBySnapshotBlock(addr, _preSnapshotBlock).send();
            List<Address> vl = send2.getValue1().getValue();
            List<Uint256> nl = send2.getValue2().getValue();
            for (int i = 0; i < vl.size(); i++) {
                String voterAddr = vl.get(i).getValue();
                BigDecimal percent = voterPercent.get(voterAddr);
                if (BigDecimal.ZERO.equals(percent)) {
                    continue;
                }
                BigDecimal num = new BigDecimal(nl.get(i).getValue());
                if (BigDecimal.ZERO.equals(num)) {
                    continue;
                }
                BigDecimal v = num.multiply(percent);
                HpbBallotVote hpbBallotVote = new HpbBallotVote(voterAddr, addr.getValue(), v.toBigInteger());
                list.add(hpbBallotVote);
            }
        }
        return list;
    }

    /**
     * 得到投票比例
     *
     * @return
     * @throws Exception
     */
    private Map<String, BigDecimal> getVoterPercent() throws Exception {
        Hpbballot hpbballot = getHpbballot();
        BigInteger currentIndex = hpbballot.fechCurrentSnapshotBlockIndex().send().getValue();
        BigInteger preIndex = currentIndex.subtract(BigInteger.ONE);
        log.info("前一个轮次：{}", preIndex);
        // 前一个快照号
        Uint256 _preSnapshotBlock = hpbballot.fechSnapshotBlockByIndex(new Uint256(preIndex)).send();
        log.info("前一个轮次快照号：{}", _preSnapshotBlock.getValue());
        Tuple3<DynamicArray<Address>, DynamicArray<Uint256>, DynamicArray<Uint256>> send = hpbballot
                .fechAllVotersBySnapshotBlock(_preSnapshotBlock).send();
        Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
        List<Address> oldAddrs = send.getValue1().getValue();
        List<Uint256> oldVoteNumbers = send.getValue3().getValue();//voteNumbers
        List<Address> addrs = new LinkedList<Address>();
        List<Uint256> voteNumbers = new LinkedList<Uint256>();
        //去除投票为零的记录
        for (int q = 0; q < oldVoteNumbers.size(); q++) {
            Uint256 uint256 = oldVoteNumbers.get(q);
            if (uint256 != null) {
                if (!BigInteger.ZERO.equals(uint256.getValue())) {
                    addrs.add(oldAddrs.get(q));
                    voteNumbers.add(oldVoteNumbers.get(q));
                }
            }
        }

        Map<String, BigInteger> voterSnapshotBalance = getVoterSnapshotBalance(addrs);
        //设置快照余额
        setSnapshotBalanceBatch(voterSnapshotBalance);
        for (int i = 0; i < addrs.size(); i++) {
            String address = addrs.get(i).getValue();
            // 前一个快照对应的投票数
            BigInteger voteNumber = voteNumbers.get(i).getValue();
            if (voteNumber.equals(BigInteger.ZERO)) {
                map.put(address, BigDecimal.ZERO);
            } else {
                // 当前快照的余额
                BigInteger bigBalance = voterSnapshotBalance.get(address);
                if (bigBalance.equals(BigInteger.ZERO)) {// 如果当前快照的余额为0，取0，不投票
                    map.put(address, BigDecimal.ZERO);
                } else if (bigBalance.compareTo(MIN_BALANCE) < 0) {// 如果当前快照的余额小于最小额，不投票
                    map.put(address, BigDecimal.ZERO);
                } else if (bigBalance.compareTo(voteNumber) >= 0) {// 如果当前快照的余额大于投票数，取100%
                    map.put(address, BigDecimal.ONE);
                } else {// 取得百分比
                    BigDecimal bigDecBalance = new BigDecimal(bigBalance);
                    BigDecimal bigDecVoteNumber = new BigDecimal(voteNumber);
                    BigDecimal percent = bigDecBalance.divide(bigDecVoteNumber, 10, RoundingMode.DOWN);
                    map.put(address, percent);
                }
            }
        }
        return map;
    }


    public boolean setHpbballotAddrToFechHpbBallotAddr() throws Exception {
        FechHpbBallotAddr fechHpbBallotAddr = getFechHpbBallotAddr();
        Address _contractAddr = new Address(hpbBallotProperties.getHpbBallotContractAddress());
        TransactionReceipt send = fechHpbBallotAddr.setContractAddr(_contractAddr).send();
        return checkSucess(send.getStatus());
    }

    public boolean addAdminToToFechHpbBallotAddr(String adminAddr) throws Exception {
        FechHpbBallotAddr fechHpbBallotAddr = getFechHpbBallotAddr();
        TransactionReceipt send = fechHpbBallotAddr.addAdmin(new Address(adminAddr)).send();
        return checkSucess(send.getStatus());
    }

    /**
     * 设置快照号
     *
     * @return
     * @throws Exception
     */
    public boolean changeStageBlock() throws Exception {
        Hpbballot hpbballot = getHpbballot();
        Uint256 currentRound = hpbballot.fechCurrentSnapshotBlockIndex().send();
        Tuple2<Uint256, Uint256> tuple2 = hpbballot.voteStages(currentRound).send();
        //当前轮次的开始区块号
        long roundNum = tuple2.getValue2().getValue().longValue();
        //当前区块号
        HpbBlockNumber blockNumber = admin.hpbBlockNumber().send();
        long currentBlockNumber = blockNumber.getBlockNumber().longValue();
        long snapshotNumber = RandomUtils.nextLong(roundNum + 6000, currentBlockNumber);
        log.info(roundNum + "," + currentBlockNumber + "," + snapshotNumber);
        BigInteger currentRoundNum = currentRound.getValue();

        log.info("开始切换轮次，新轮次为{}", currentRoundNum.add(BigInteger.ONE));
        TransactionReceipt send = hpbballot.changeStageBlock(new Uint256(snapshotNumber)).send();
        return checkSucess(send.getStatus());

    }

    /**
     * 批量代投票
     *
     * @return
     * @throws Exception
     */
    public boolean voteNoLockByAdminBatch() throws Exception {
        log.info("开始批量代投票");
        List<HpbBallotVote> batchVotes = getBatchVote();
        if (CollectionUtils.isEmpty(batchVotes)) {
            return false;
        }
        Hpbballot hpbballot = getHpbballot();
        List<Uint256> numList = new LinkedList<Uint256>();
        List<Address> voterAddrList = new LinkedList<Address>();
        List<Address> candidateAddrList = new LinkedList<Address>();
        int voteNoLockByAdminNum = 0;
        boolean checkSucess = false;
        for (HpbBallotVote hpbBallotVote : batchVotes) {
            numList.add(new Uint256(hpbBallotVote.getNum()));
            voterAddrList.add(new Address(hpbBallotVote.getVoterAddr()));
            candidateAddrList.add(new Address(hpbBallotVote.getCandidateAddr()));
            if (numList.size() > SCALE1) {
                DynamicArray<Uint256> nums = new DynamicArray<Uint256>(numList);
                DynamicArray<Address> voterAddrs = new DynamicArray<Address>(voterAddrList);
                DynamicArray<Address> candidateAddrs = new DynamicArray<Address>(candidateAddrList);
                TransactionReceipt send = hpbballot.voteNoLockByAdminBatch(voterAddrs, candidateAddrs, nums).send();
                log.info("{}", send);
                voteNoLockByAdminNum++;
                log.info("voteNoLockByAdminNum:{}", voteNoLockByAdminNum);
                numList = null;
                voterAddrList = null;
                candidateAddrList = null;
                numList = new LinkedList<Uint256>();
                voterAddrList = new LinkedList<Address>();
                candidateAddrList = new LinkedList<Address>();
                checkSucess = checkSucess(send.getStatus());
                if (!checkSucess) {
                    throw new Exception("代投失败");
                }
            }
        }
        if (numList.size() > 0) {
            DynamicArray<Uint256> nums = new DynamicArray<Uint256>(numList);
            DynamicArray<Address> voterAddrs = new DynamicArray<Address>(voterAddrList);
            DynamicArray<Address> candidateAddrs = new DynamicArray<Address>(candidateAddrList);
            TransactionReceipt send = hpbballot.voteNoLockByAdminBatch(voterAddrs, candidateAddrs, nums).send();
            log.info("{}", send);
            voteNoLockByAdminNum++;
            log.info("voteNoLockByAdminNum:{}", voteNoLockByAdminNum);
            checkSucess = checkSucess(send.getStatus());
        }
        return checkSucess;
    }

    public List<Object> DeployHpbBallotContract(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null) {
            if (reqStrList.size() > 0) {
                String clientAddress = reqStrList.get(0);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 1) {
                    password = reqStrList.get(1);
                }
            }
        }
        RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
        HpbBlockNumber blockNumber = newAdmin.hpbBlockNumber().send();
        Uint256 _startBlock = new Uint256(blockNumber.getBlockNumber());
        Uint256 _endBlock = new Uint256(blockNumber.getBlockNumber().add(new BigInteger("10000000")));
        ;
        Uint256 _capacity = new Uint256(new BigInteger("105"));
        ;
        Uint256 _version = new Uint256(new BigInteger("1"));
        Hpbballot hpbballot = Hpbballot.deploy(newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT,
                BigInteger.ZERO, _startBlock, _endBlock, _capacity, _version).send();
        String contractAddress = hpbballot.getContractAddress();
        list.add(contractAddress);
        return list;
    }

    public List<Object> DeployInnerContract(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null) {
            if (reqStrList.size() > 0) {
                String clientAddress = reqStrList.get(0);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 1) {
                    password = reqStrList.get(1);
                }
            }
        }
        RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
        FechHpbBallotAddr fechHpbBallotAddr = FechHpbBallotAddr.deploy(newAdmin,
                transactionManager, GAS_PRICE, GAS_LIMIT).send();
        String contractAddress = fechHpbBallotAddr.getContractAddress();
        list.add(contractAddress);
        return list;
    }

    private Web3Service buildService(String clientAddress) {
        Web3Service web3Service;
        if (clientAddress == null) {
            web3Service = new HttpService();
        } else if (clientAddress.startsWith("http")) {
            web3Service = new HttpService(clientAddress);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            web3Service = new WindowsIpcService(clientAddress);
        } else {
            web3Service = new UnixIpcService(clientAddress);
        }
        return web3Service;
    }

    public List<Object> SetHpbBallotContractToInnerContract(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 1) {
            if (reqStrList.size() > 2) {
                String clientAddress = reqStrList.get(2);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 3) {
                    password = reqStrList.get(3);
                }
            }
            String innerContractAddress = reqStrList.get(0);
            String contractAddr = reqStrList.get(1);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            FechHpbBallotAddr fechHpbBallotAddr = FechHpbBallotAddr.load(
                    innerContractAddress, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            TransactionReceipt send = fechHpbBallotAddr.setContractAddr(
                    new Address(contractAddr)).send();
            list.add(send);
        }
        return list;
    }

    public List<Object> InitHpbBallotContract(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 2) {
            if (reqStrList.size() > 3) {
                String clientAddress = reqStrList.get(3);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 4) {
                    password = reqStrList.get(4);
                }
            }
            Uint256 _round = new Uint256(new BigInteger(reqStrList.get(0)));
            Uint256 _capacity = new Uint256(new BigInteger(reqStrList.get(1)));
            String contractAddr = reqStrList.get(2);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Uint256 _startBlock = hpbballot.startBlock().send();
            Uint256 _endBlock = hpbballot.endBlock().send();
            Uint256 _version = hpbballot.version().send();
            TransactionReceipt send2 = hpbballot.updateContract(_startBlock, _endBlock, _capacity, _version).send();
            list.add(send2);
            TransactionReceipt send = hpbballot.setRound(_round).send();
            list.add(send);
        }
        return list;
    }


    public List<Object> ChangStage(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 2) {
                    password = reqStrList.get(2);
                }
            }
            String contractAddr = reqStrList.get(0);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Uint256 currentRound = hpbballot.fechCurrentSnapshotBlockIndex().send();
            Tuple2<Uint256, Uint256> tuple2 = hpbballot.voteStages(currentRound).send();

            // 当前轮次的开始区块号
            long roundNum = tuple2.getValue2().getValue().longValue();
            //当前区块号
            long currentBlockNumber = newAdmin.hpbBlockNumber().send().getBlockNumber().longValue();
            long nextLong = RandomUtils.nextLong(roundNum, currentBlockNumber);
            log.info("{},{},{}", roundNum, currentBlockNumber, nextLong);
            TransactionReceipt send = hpbballot.changeStageBlock(new Uint256(nextLong)).send();
            list.add(send);
            TransactionReceipt receipt = voteNoLockByAdminBatch(hpbballot, newAdmin);
            list.add(receipt);
        }
        return list;
    }

    public List<Object> QueryAllCandidates(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 2) {
                    password = reqStrList.get(2);
                }
            }
            String contractAddr = reqStrList.get(0);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Uint256 _snapshotBlock = hpbballot.currentSnapshotBlock().send();
            Tuple3<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>> send7 = hpbballot.fechAllCandidatesBySnapshotBlock(_snapshotBlock).send();
            List<Address> v1 = send7.getValue1().getValue();
            List<Bytes32> v2 = send7.getValue2().getValue();
            List<Bytes32> v3 = send7.getValue3().getValue();
            for (int i = 0; i < v1.size(); i++) {
                list.add(v1.get(i).getValue() + " " + new String(v2.get(i).getValue(), StandardCharsets.UTF_8) +
                        " " + new String(v3.get(i).getValue(), StandardCharsets.UTF_8));
            }
        }
        return list;
    }

    /**
     * 批量代投票
     *
     * @return
     * @throws Exception
     */
    public TransactionReceipt voteNoLockByAdminBatch(Hpbballot hpbballot, Admin newAdmin) throws Exception {
        List<HpbBallotVote> batchVotes = getBatchVote(hpbballot, newAdmin);
        if (CollectionUtils.isEmpty(batchVotes)) {
            return null;
        }
        List<Uint256> numList = new LinkedList<Uint256>();
        List<Address> voterAddrList = new LinkedList<Address>();
        List<Address> candidateAddrList = new LinkedList<Address>();
        int voteNoLockByAdminNum = 0;
        for (HpbBallotVote hpbBallotVote : batchVotes) {
            numList.add(new Uint256(hpbBallotVote.getNum()));
            voterAddrList.add(new Address(hpbBallotVote.getVoterAddr()));
            candidateAddrList.add(new Address(hpbBallotVote.getCandidateAddr()));
            if (numList.size() > SCALE1) {
                DynamicArray<Uint256> nums = new DynamicArray<Uint256>(numList);
                DynamicArray<Address> voterAddrs = new DynamicArray<Address>(voterAddrList);
                DynamicArray<Address> candidateAddrs = new DynamicArray<Address>(candidateAddrList);
                TransactionReceipt send = hpbballot.voteNoLockByAdminBatch(voterAddrs, candidateAddrs, nums).send();
                log.info(send.toString());
                voteNoLockByAdminNum++;
                log.info("voteNoLockByAdminNum:{}", voteNoLockByAdminNum + "");
                numList = null;
                voterAddrList = null;
                candidateAddrList = null;
                numList = new LinkedList<Uint256>();
                voterAddrList = new LinkedList<Address>();
                candidateAddrList = new LinkedList<Address>();
            }
        }

        DynamicArray<Uint256> nums = new DynamicArray<Uint256>(numList);
        DynamicArray<Address> voterAddrs = new DynamicArray<Address>(voterAddrList);
        DynamicArray<Address> candidateAddrs = new DynamicArray<Address>(candidateAddrList);
        TransactionReceipt send = hpbballot.voteNoLockByAdminBatch(voterAddrs, candidateAddrs, nums).send();
        log.info(send.toString());
        voteNoLockByAdminNum++;
        log.info("voteNoLockByAdminNum:{}", voteNoLockByAdminNum + "");
        return send;
    }

    public void setSnapshotBalanceBatch(Hpbballot hpbballot, Admin newAdmin,
                                        Map<String, BigInteger> voterSnapshotBalance) throws Exception {
        if (MapUtils.isEmpty(voterSnapshotBalance)) {
            return;
        }
        List<Uint256> voterSnapshot = new LinkedList<Uint256>();
        List<Address> newAddrs = new LinkedList<Address>();
        int setSnapshotBalanceNum = 0;
        for (Map.Entry<String, BigInteger> en : voterSnapshotBalance.entrySet()) {
            //当前快照的余额
            BigInteger bigBalance = en.getValue();
            voterSnapshot.add(new Uint256(bigBalance));
            newAddrs.add(new Address(en.getKey()));
            if (newAddrs.size() > SCALE) {
                DynamicArray<Uint256> _snapshotBalances = new DynamicArray<Uint256>(voterSnapshot);
                DynamicArray<Address> _voterAddrs = new DynamicArray<Address>(newAddrs);
                TransactionReceipt send = hpbballot.setSnapshotBalanceBatch(_voterAddrs, _snapshotBalances).send();
                log.info(send.getLogs().toString());
                setSnapshotBalanceNum++;
                log.info(setSnapshotBalanceNum + "");
                newAddrs = null;
                voterSnapshot = null;
                voterSnapshot = new LinkedList<Uint256>();
                newAddrs = new LinkedList<Address>();
            }
        }
        if (newAddrs.size() > 0) {
            DynamicArray<Uint256> _snapshotBalances = new DynamicArray<Uint256>(voterSnapshot);
            DynamicArray<Address> _voterAddrs = new DynamicArray<Address>(newAddrs);
            TransactionReceipt send = hpbballot.setSnapshotBalanceBatch(_voterAddrs, _snapshotBalances).send();
            log.info(send.getLogs().toString());
            setSnapshotBalanceNum++;
            log.info(setSnapshotBalanceNum + "");
        }
    }

    /**
     * @return 组装需要代投票的数据
     * @throws Exception
     */
    public List<HpbBallotVote> getBatchVote(Hpbballot hpbballot, Admin newAdmin) throws Exception {
        Map<String, BigDecimal> voterPercent = getVoterPercent(hpbballot, newAdmin);
        BigInteger currentIndex = hpbballot.fechCurrentSnapshotBlockIndex().send().getValue();
        BigInteger preIndex = currentIndex.subtract(BigInteger.ONE);
        //前一个快照号
        Uint256 _preSnapshotBlock = hpbballot.fechSnapshotBlockByIndex(new Uint256(preIndex)).send();

        //当前快照号
        Uint256 _currentSnapshotBlock = hpbballot.currentSnapshotBlock().send();
        List<HpbBallotVote> list = new ArrayList<HpbBallotVote>();
        Tuple2<DynamicArray<Address>, DynamicArray<Uint256>> send3 = hpbballot
                .fechAllVoteResultBySnapshotBlock(_currentSnapshotBlock).send();
        List<Address> oldAddrs = send3.getValue1().getValue();
        List<Uint256> oldNums = send3.getValue2().getValue();
        List<Address> addrs = new LinkedList<Address>();
        for (int i = 0; i < oldAddrs.size(); i++) {
            if (oldNums.get(i) != null) {
                if (!BigInteger.ZERO.equals(oldNums.get(i).getValue())) {
                    addrs.add(oldAddrs.get(i));
                }
            }
        }
        for (Address addr : addrs) {
            Tuple2<DynamicArray<Address>, DynamicArray<Uint256>> send2 = hpbballot.
                    fechVoteResultForCandidateBySnapshotBlock(addr, _preSnapshotBlock).send();
            List<Address> vl = send2.getValue1().getValue();
            List<Uint256> nl = send2.getValue2().getValue();
            for (int i = 0; i < vl.size(); i++) {
                String voterAddr = vl.get(i).getValue();
                BigDecimal percent = voterPercent.get(voterAddr);
                if (BigDecimal.ZERO.equals(percent)) {
                    continue;
                }
                BigDecimal num = new BigDecimal(nl.get(i).getValue());
                if (BigDecimal.ZERO.equals(num)) {
                    continue;
                }
                BigDecimal v = num.multiply(percent);
                HpbBallotVote hpbBallotVote = new HpbBallotVote(voterAddr, addr.getValue(), v.toBigInteger());
                list.add(hpbBallotVote);
            }
        }
        return list;
    }

    /**
     * 得到投票比例
     *
     * @return
     * @throws Exception
     */
    public Map<String, BigDecimal> getVoterPercent(Hpbballot hpbballot, Admin admin) throws Exception {
        BigInteger currentIndex = hpbballot.fechCurrentSnapshotBlockIndex().send().getValue();
        BigInteger preIndex = currentIndex.subtract(BigInteger.ONE);

        //前一个快照号
        Uint256 _preSnapshotBlock = hpbballot.fechSnapshotBlockByIndex(new Uint256(preIndex)).send();

        Tuple3<DynamicArray<Address>, DynamicArray<Uint256>, DynamicArray<Uint256>> send =
                hpbballot.fechAllVotersBySnapshotBlock(_preSnapshotBlock).send();
        Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
        List<Address> v1 = send.getValue1().getValue();
        List<Uint256> v3 = send.getValue3().getValue();//voteNumbers
        Map<String, BigInteger> voterSnapshotBalance = getVoterSnapshotBalance(v1, hpbballot, admin);
        setSnapshotBalanceBatch(hpbballot, admin, voterSnapshotBalance);
        for (int i = 0; i < v1.size(); i++) {
            String address = v1.get(i).getValue();
            //前一个快照对应的投票数
            BigInteger voteNumber = v3.get(i).getValue();
            if (voteNumber.equals(BigInteger.ZERO)) {
                map.put(address, BigDecimal.ZERO);
            } else {
                //当前快照的余额
                BigInteger bigBalance = voterSnapshotBalance.get(address);
                if (bigBalance.equals(BigInteger.ZERO)) {//如果当前快照的余额为0，取0，不投票
                    map.put(address, BigDecimal.ZERO);
                } else if (bigBalance.compareTo(MIN_BALANCE) < 0) {//如果当前快照的余额小于最小额，不投票
                    map.put(address, BigDecimal.ZERO);
                } else if (bigBalance.compareTo(voteNumber) >= 0) {//如果当前快照的余额大于投票数，取100%
                    map.put(address, BigDecimal.ONE);
                } else {//取得百分比
                    BigDecimal bigDecBalance = new BigDecimal(bigBalance);
                    BigDecimal bigDecVoteNumber = new BigDecimal(voteNumber);
                    BigDecimal percent = bigDecBalance.divide(bigDecVoteNumber, 10, RoundingMode.DOWN);
                    map.put(address, percent);
                }
            }
        }
        return map;
    }

    private Map<String, BigInteger> getVoterSnapshotBalance(List<Address> addrs,
                                                            Hpbballot hpbballot, Admin newAdmin) throws Exception {
        Map<String, BigInteger> snapshotBalance = new ConcurrentHashMap<String, BigInteger>();
        if (CollectionUtils.isEmpty(addrs)) {
            return snapshotBalance;
        }
        BigInteger _currentSnapshotBlock = hpbballot.currentSnapshotBlock().send().getValue();
        Instant start = Instant.now();
        int size = addrs.size();
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (Address addr : addrs) {
            if (snapshotBalance.get(addr.getValue()) != null) {
                countDownLatch.countDown();
            } else {
                Thread.sleep(10);
                asyncTask.getVoterSnapshotBalanceFromNode(countDownLatch, addr,
                        snapshotBalance, _currentSnapshotBlock, newAdmin);
            }
        }
        countDownLatch.await(100, TimeUnit.MINUTES);
        Duration spend = Duration.between(start, Instant.now());
        log.info("从区块链节点上获取{}个地址快照{}余额信息,花费时间：{}", size, _currentSnapshotBlock,
                DurationFormatUtils.formatDuration(spend.toMillis(), "ss.SSS"));
        return snapshotBalance;
    }


    public List<Object> FechAllVoteResultForCurrent(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 2) {
                    password = reqStrList.get(2);
                }
            }
            String contractAddr = reqStrList.get(0);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Tuple2<DynamicArray<Address>, DynamicArray<Uint256>> send = hpbballot.
                    fechAllVoteResultForCurrent().send();
            List<Address> value1 = send.getValue1().getValue();
            List<Uint256> value2 = send.getValue2().getValue();
            for (int i = 0; i < value1.size(); i++) {
                list.add(value1.get(i).getValue() + ":   " + value2.get(i).getValue());
            }
        }
        return list;
    }

    public List<Object> FechAllVoteResultPreStageByBlock(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 1) {
            if (reqStrList.size() > 2) {
                String clientAddress = reqStrList.get(2);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 3) {
                    password = reqStrList.get(3);
                }
            }
            String contractAddr = reqStrList.get(0);
            Uint256 _startBlock = new Uint256(new BigInteger(reqStrList.get(1)));
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Tuple4<Uint256, Uint256, DynamicArray<Address>, DynamicArray<Uint256>> send5 =
                    hpbballot.fechAllVoteResultPreStageByBlock(_startBlock).send();
            BigInteger value1 = send5.getValue1().getValue();
            BigInteger value2 = send5.getValue2().getValue();
            list.add(value1);
            list.add(value2);
            List<Address> value3 = send5.getValue3().getValue();
            List<Uint256> value4 = send5.getValue4().getValue();
            for (int i = 0; i < value3.size(); i++) {
                list.add(value3.get(i).getValue() + "  " + value4.get(i).getValue());
            }
        }
        return list;
    }

    public List<Object> FechAllVoteResultBySnapshotBlock(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 1) {
            if (reqStrList.size() > 2) {
                String clientAddress = reqStrList.get(2);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 3) {
                    password = reqStrList.get(3);
                }
            }
            String contractAddr = reqStrList.get(0);
            Uint256 _snapshotBlock = new Uint256(new BigInteger(reqStrList.get(1)));
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Tuple2<DynamicArray<Address>, DynamicArray<Uint256>> send5 =
                    hpbballot.fechAllVoteResultBySnapshotBlock(_snapshotBlock).send();
            List<Address> value3 = send5.getValue1().getValue();
            List<Uint256> value4 = send5.getValue2().getValue();
            for (int i = 0; i < value3.size(); i++) {
                list.add(value3.get(i).getValue() + "  " + value4.get(i).getValue());
            }
        }
        return list;
    }

    public List<Object> CurrentSnapshotBlock(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 2) {
                    password = reqStrList.get(2);
                }
            }
            String contractAddr = reqStrList.get(0);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Uint256 send = hpbballot.currentSnapshotBlock().send();
            list.add(send.getValue());
        }
        return list;
    }

    public List<Object> VoteResult(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 2) {
                    password = reqStrList.get(2);
                }
            }
            String contractAddr = reqStrList.get(0);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            TransactionReceipt send = hpbballot.voteResult().send();
            list.add(send);
            Tuple3<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Uint256>> send8 =
                    hpbballot.getVoteResult().send();
            List<Address> v1 = send8.getValue1().getValue();
            List<Bytes32> v2 = send8.getValue2().getValue();
            List<Uint256> v3 = send8.getValue3().getValue();
            for (int i = 0; i < v1.size(); i++) {
                list.add(v1.get(i).getValue() + " " + new String(v2.get(i).getValue()) + " " +
                        v3.get(i).getValue());
            }
        }
        return list;
    }

    public List<Object> VoteNoLockByAdmin(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 3) {
            if (reqStrList.size() > 4) {
                String clientAddress = reqStrList.get(4);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 5) {
                    password = reqStrList.get(5);
                }
            }
            String contractAddr = reqStrList.get(0);
            Address voterAddr = new Address(reqStrList.get(1));
            Address candidateAddr = new Address(reqStrList.get(2));
            Uint256 num = new Uint256(new BigInteger(reqStrList.get(3)));
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Uint256 _snapshotBalance = new Uint256(Convert.toWei("99000000", Convert.Unit.HPB).toBigInteger());
            hpbballot.setSnapshotBalance(voterAddr, _snapshotBalance).send();

            TransactionReceipt send = hpbballot.voteNoLockByAdmin(voterAddr, candidateAddr, num).send();
            list.add(send);
        }
        return list;
    }

    public List<Object> QueryByHash(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
            }
            String hash = reqStrList.get(0);
            HpbGetTransactionReceipt send = newAdmin.hpbGetTransactionReceipt(hash).send();
            list.add(send);
        }
        return list;
    }

    public List<Object> VoteBatch(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 2) {
                    password = reqStrList.get(2);
                }
            }
            String contractAddr = reqStrList.get(0);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Uint256 _snapshotBalance = new Uint256(Convert.toWei("99000000", Convert.Unit.HPB).toBigInteger());
            List<String> readLines = FileUtils.readLines(new File("D:/test.txt"), StandardCharsets.UTF_8);
            List<Address> la = new LinkedList<Address>();
            List<Uint256> ls = new LinkedList<Uint256>();
            Map<Integer, List<Address>> mm = new HashMap<Integer, List<Address>>();

            List<Address> ll = null;
            int j = 0;
            for (int k = 0; k < readLines.size(); k++) {
                Address ars = new Address(readLines.get(k));
                la.add(ars);
                ls.add(_snapshotBalance);
                if (la.size() > SCALE) {
                    DynamicArray<Address> voterAddrs = new DynamicArray<Address>(la);
                    DynamicArray<Uint256> _snapshotBalances = new DynamicArray<Uint256>(ls);
                    TransactionReceipt send2 = hpbballot.setSnapshotBalanceBatch(voterAddrs, _snapshotBalances).send();
                    log.info(send2.toString());
                    la = null;
                    ls = null;
                    la = new LinkedList<Address>();
                    ls = new LinkedList<Uint256>();
                }
                if (k % 24 == 0) {
                    ll = new ArrayList<Address>();
                    mm.put(j, ll);
                    j++;
                }
                ll.add(ars);
            }
            if (la.size() > 0) {
                DynamicArray<Address> voterAddrs = new DynamicArray<Address>(la);
                DynamicArray<Uint256> _snapshotBalances = new DynamicArray<Uint256>(ls);
                TransactionReceipt send2 = hpbballot.setSnapshotBalanceBatch(voterAddrs, _snapshotBalances).send();
                log.info(send2.toString());
            }
            readLines = null;
            ls = null;

            Uint256 _currentSnapshotBlock = hpbballot.currentSnapshotBlock().send();
            Tuple3<DynamicArray<Address>, DynamicArray<Bytes32>, DynamicArray<Bytes32>> send3 =
                    hpbballot.fechAllCandidatesBySnapshotBlock(_currentSnapshotBlock).send();
            List<Address> candidateAddrList = send3.getValue1().getValue();
            log.info(send3.toString());

            List<Uint256> numList = new LinkedList<Uint256>();
            List<Address> voterAddrList = new LinkedList<Address>();
            List<Address> candidateAddrsList = new LinkedList<Address>();
            for (int i = 0; i < candidateAddrList.size(); i++) {
                List<Address> list2 = mm.get(i);
                for (Address addr : list2) {
                    candidateAddrsList.add(candidateAddrList.get(i));
                    voterAddrList.add(addr);
                    numList.add(new Uint256(Convert.toWei(new BigDecimal(
                                    RandomUtils.nextLong(1L, 5L)),
                            Convert.Unit.HPB).toBigInteger()));
                    if (candidateAddrsList.size() > SCALE1) {
                        DynamicArray<Uint256> nums = new DynamicArray<Uint256>(numList);
                        DynamicArray<Address> voterAddrs1 = new DynamicArray<Address>(voterAddrList);
                        DynamicArray<Address> candidateAddrs = new DynamicArray<Address>(candidateAddrsList);
                        TransactionReceipt send = hpbballot.voteNoLockByAdminBatch(voterAddrs1, candidateAddrs, nums).send();
                        log.info(send.toString());
                        candidateAddrsList = null;
                        voterAddrList = null;
                        numList = null;
                        numList = new LinkedList<Uint256>();
                        voterAddrList = new LinkedList<Address>();
                        candidateAddrsList = new LinkedList<Address>();
                    }
                }
            }
            if (numList.size() > 0) {
                DynamicArray<Uint256> nums = new DynamicArray<Uint256>(numList);
                DynamicArray<Address> voterAddrs1 = new DynamicArray<Address>(voterAddrList);
                DynamicArray<Address> candidateAddrs = new DynamicArray<Address>(candidateAddrsList);
                TransactionReceipt send = hpbballot.voteNoLockByAdminBatch(voterAddrs1, candidateAddrs, nums).send();
                log.info(send.toString());
            }
        }
        return list;
    }

    public List<Object> FechCurrentSnapshotBlockIndex(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        String password = null;
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
                if (reqStrList.size() > 2) {
                    password = reqStrList.get(2);
                }
            }
            String contractAddr = reqStrList.get(0);
            RawTransactionManager transactionManager = getTransactionManager(password, newAdmin);
            Hpbballot hpbballot = Hpbballot.load(
                    contractAddr, newAdmin, transactionManager, GAS_PRICE, GAS_LIMIT);
            Uint256 send = hpbballot.fechCurrentSnapshotBlockIndex().send();
            list.add(send.getValue());
        }
        return list;
    }

    public List<Object> getCurrentBlock(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            String clientAddress = reqStrList.get(0);
            if (StringUtils.isNotBlank(clientAddress)) {
                newAdmin = Admin.build(buildService(clientAddress));
            }
        }
        HpbBlockNumber send = newAdmin.hpbBlockNumber().send();
        list.add(send.getBlockNumber());
        return list;
    }

    public List<Object> getCurrentNonce(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
            }
            String address = reqStrList.get(0);
            HpbGetTransactionCount send = newAdmin.hpbGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            list.add(send);
        }
        return list;
    }

    public List<Object> getBalance(List<String> reqStrList) throws Exception {
        List<Object> list = new ArrayList<Object>();
        Admin newAdmin = admin;
        if (reqStrList != null && reqStrList.size() > 0) {
            if (reqStrList.size() > 1) {
                String clientAddress = reqStrList.get(1);
                if (StringUtils.isNotBlank(clientAddress)) {
                    newAdmin = Admin.build(buildService(clientAddress));
                }
            }
            String address = reqStrList.get(0);
            HpbGetBalance send = newAdmin.hpbGetBalance(address, DefaultBlockParameterName.LATEST).send();
            list.add(send);
        }
        return list;
    }
}