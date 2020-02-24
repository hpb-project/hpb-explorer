package com.hpb.bc.async;

import com.hpb.bc.constant.BcConstant;
import com.hpb.bc.contracts.abi.EventHelper;
import com.hpb.bc.contracts.abi.EventValuesWithLog;
import com.hpb.bc.entity.Addrs;
import com.hpb.bc.entity.BlockAddrs;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.model.EventData;
import com.hpb.bc.model.HpbEventModel;
import com.hpb.bc.model.HpbHash;
import com.hpb.bc.page.HpbPageHelper;
import com.hpb.bc.propeller.HpbPropellerFacade;
import com.hpb.bc.propeller.model.EvmdiffLog;
import com.hpb.bc.propeller.model.HpbStatediffbyblockandTx;
import com.hpb.bc.propeller.model.HpbStatediffbyblockandTxModel;
import com.hpb.bc.propeller.model.StateLog;
import com.hpb.bc.service.AddrsService;
import com.hpb.bc.service.BlockAddrsService;
import com.hpb.bc.service.TxTransferRecordService;
import com.hpb.bc.util.ContractEventHepler;
import com.hpb.bc.util.FastJsonUtils;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.methods.response.*;
import io.hpb.web3.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Component
public class AsyncContractEventTask {
    public static final int TIME_OUT = 2;
    public Logger log = LoggerFactory.getLogger(AsyncContractEventTask.class);
    @Autowired
    ContractEventHepler contractEventHepler;

    @Autowired
    HpbPropellerFacade hpbPropellerFacade;

    @Autowired
    EventHelper eventHelper;

    @Autowired
    AddrsService addrsService;

    @Autowired
    BlockAddrsService blockAddrsService;

    @Autowired
    HpbPageHelper hpbPageHelper;

    @Autowired
    TxTransferRecordService txTransferRecordService;
    @Autowired
    private Admin admin;

    @Async
    public void getEventDataLogByAddress(CountDownLatch countDownLatch, List<EventData> eventDataList, TxTransferRecord record) {
        try {
            HpbGetTransactionReceipt transactionReceipt = admin.hpbGetTransactionReceipt(record.getTxHash()).sendAsync().get(2, TimeUnit.MINUTES);
            TransactionReceipt receipt = null;
            if (transactionReceipt.getTransactionReceipt() != null && transactionReceipt.getTransactionReceipt().isPresent()) {
                receipt = transactionReceipt.getTransactionReceipt().get();
            }
            HpbHash hpbHash = HpbHash.of(receipt.getTransactionHash());
            List<Log> logs = receipt.getLogs();
            List<EventData> eventDataList2 = contractEventHepler.toEventInfos(hpbHash, logs);
            eventDataList.addAll(eventDataList2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }


    public List<StateLog> getStateDiffByTxHash(String txHash) {
        log.info("getEventLogAndStateDiffByTxHash  txHash [{}]", txHash);
        HpbStatediffbyblockandTx hpbStatediffbyblockandTx = hpbPropellerFacade.getHpbStatediffbyblockandTxByBlockNumberOrTxHash(null, txHash);
        if (hpbStatediffbyblockandTx == null || BcConstant.EMPTY_MIDDLE_BRACKET.equals(hpbStatediffbyblockandTx.getResult())) {
            return new ArrayList<>();
        }
        List<StateLog> stateLogList = new ArrayList<>();
        log.info(" hpbStatediffbyblockandTx.getResult() ==== " + hpbStatediffbyblockandTx.getResult());
        HpbStatediffbyblockandTxModel model = FastJsonUtils.getJsonToBean(hpbStatediffbyblockandTx.getResult(), HpbStatediffbyblockandTxModel.class);
        stateLogList = model.getState_diff();
        return stateLogList;
    }


    public List<EvmdiffLog> getEvmdiffLogByTxHash(String txHash) {
        log.info("getEventLogAndStateDiffByTxHash  txHash [{}]", txHash);
        HpbStatediffbyblockandTx hpbStatediffbyblockandTx = hpbPropellerFacade.getHpbStatediffbyblockandTxByBlockNumberOrTxHash(null, txHash);

        if (hpbStatediffbyblockandTx == null || BcConstant.EMPTY_MIDDLE_BRACKET.equals(hpbStatediffbyblockandTx.getResult())) {
            return new ArrayList<>();
        }
        log.info(" hpbStatediffbyblockandTx.getResult() ==== " + hpbStatediffbyblockandTx.getResult());
        HpbStatediffbyblockandTxModel model = FastJsonUtils.getJsonToBean(hpbStatediffbyblockandTx.getResult(), HpbStatediffbyblockandTxModel.class);
        List<EvmdiffLog> evmdiffLogList = FastJsonUtils.getJsonToList(model.getEvmdiff(), EvmdiffLog.class);
        return evmdiffLogList;
    }

    public List<EventData> getEventDataLogByTxHash(String txHash) {
        try {
            HpbGetTransactionReceipt transactionReceipt = admin.hpbGetTransactionReceipt(txHash).sendAsync().get(2, TimeUnit.MINUTES);
            TransactionReceipt receipt = null;
            if (transactionReceipt.getTransactionReceipt() != null && transactionReceipt.getTransactionReceipt().isPresent()) {
                receipt = transactionReceipt.getTransactionReceipt().get();
            }
            HpbHash hpbHash = HpbHash.of(receipt.getTransactionHash());
            log.info("hpbHash raw ===" + hpbHash.data.toString());
            List<Log> logs = receipt.getLogs();
            List<EventData> eventDataList2 = contractEventHepler.toEventInfos(hpbHash, logs);
            log.info("eventDataList2 ====" + eventDataList2.size());
            return eventDataList2;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<HpbEventModel> getPageHpbEventModelByTxHash(String txHash) {
        try {
            HpbTransaction hpbTransaction = admin.hpbGetTransactionByHash(txHash).sendAsync().get(2, TimeUnit.MINUTES);
            Transaction tx = null;
            if (hpbTransaction.getTransaction().isPresent()) {
                tx = hpbTransaction.getTransaction().get();
            }
            HpbGetTransactionReceipt transactionReceipt = admin.hpbGetTransactionReceipt(txHash).sendAsync().get(2, TimeUnit.MINUTES);
            TransactionReceipt receipt = null;
            if (transactionReceipt.getTransactionReceipt() != null && transactionReceipt.getTransactionReceipt().isPresent()) {
                receipt = transactionReceipt.getTransactionReceipt().get();
            }
            String abi = "";
            List<HpbEventModel> eventValuesWithLogList = eventHelper.getHpbEventModel(tx, receipt, abi);
            log.info("eventDataList2 ====" + eventValuesWithLogList.size());
            return eventValuesWithLogList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<HpbEventModel> getPageHpbEventModelByAddress(String address, int pageNum, int pageSize) {
        try {
            Addrs addrs = addrsService.getAddrsByAddress(address);
            List<BlockAddrs> finalList = getBlockAddrsWithOfTheAddress(addrs.getStartBlock(), addrs.getLastestBlock(), addrs);
            List<BlockAddrs> tempList = new ArrayList<>();
            if (finalList.size() > 50) {
                tempList = finalList.subList(0, 40);
            }
            List<Transaction> transactionList = new ArrayList<>();
            try {
                transactionList = hpbPageHelper.getTransactionInfosFromBlockAddrsByAddress(tempList, addrs.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<HpbEventModel> resultHpbEventModelList = Collections.synchronizedList(new ArrayList<HpbEventModel>());
            for (int i = 0; i < transactionList.size(); i++) {
                Transaction txLoop = transactionList.get(i);
                log.info("txHash =[" + txLoop.getHash() + ", ] , length [ " + txLoop.getInput().length() + "]");
                if (txLoop == null || txLoop.getInput().length() < 10) {
                    continue;
                }
                HpbGetTransactionReceipt transactionReceipt = admin.hpbGetTransactionReceipt(txLoop.getHash()).sendAsync().get(2, TimeUnit.MINUTES);
                TransactionReceipt receipt = null;
                if (transactionReceipt.getTransactionReceipt() != null && transactionReceipt.getTransactionReceipt().isPresent()) {
                    receipt = transactionReceipt.getTransactionReceipt().get();
                }
                String abi = "";
                List<HpbEventModel> eventValuesWithLogList = eventHelper.getHpbEventModel(txLoop, receipt, abi);
                log.info(" tx [" + i + "],hash = " + txLoop.getHash() + "，eventValuesWithLogList.size =" + eventValuesWithLogList.size());
                resultHpbEventModelList.addAll(eventValuesWithLogList);
            }
            log.info("eventDataList2 ====" + resultHpbEventModelList.size());
            return resultHpbEventModelList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<BlockAddrs> getBlockAddrsWithOfTheAddress(Long startBlockNumber, Long endBlockNumber, Addrs addrs) {
        List<BlockAddrs> blockAddrsList = new ArrayList<>();
        List<BlockAddrs> finalList = new ArrayList<>();
        blockAddrsList = blockAddrsService.getBlockAddrsListBetweenStartBlockAndEndBlock(startBlockNumber, endBlockNumber);
        BigInteger x = BigInteger.valueOf(addrs.getNumber());
        String s = Numeric.toHexStringNoPrefix(x);
        log.info("number IntToHex == " + s);
        finalList = blockAddrsList.stream().filter(e -> Arrays.asList(e.getAddrs().split(",")).contains(s)).collect(Collectors.toList());
        return finalList;
    }

}
