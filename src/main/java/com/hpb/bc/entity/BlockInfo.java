package com.hpb.bc.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hpb.bc.model.TransactionDetailModel;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.methods.response.HpbBlock.Block;
import io.hpb.web3.protocol.core.methods.response.HpbBlock.TransactionResult;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.utils.Numeric;

public class BlockInfo extends BaseEntity {
    private static final long serialVersionUID = -2095646461136260134L;

    private Long transCount;
    private Long contractCount;

    public Long getTransCount() {
        return transCount;
    }

    public void setTransCount(Long transCount) {
        this.transCount = transCount;
    }

    public Long getContractCount() {
        return contractCount;
    }

    public void setContractCount(Long contractCount) {
        this.contractCount = contractCount;
    }

    private String number;
    private String hash;
    private String parentHash;
    private String nonce;
    private String sha3Uncles;
    private String logsBloom;
    private String transactionsRoot;
    private String stateRoot;
    private String receiptsRoot;
    private String author;
    private String miner;
    private String mixHash;
    private String difficulty;
    private String totalDifficulty;
    private String extraData;
    private String size;
    private String gasLimit;
    private String gasUsed;
    private String averageGasPrice;
    private String blockGasUsed;
    private String blockGas;
    private String blockGasSpent;
    private String timestamp;
    private List<String> uncles;

    public BigInteger getNumber() {
        return Numeric.decodeQuantity(number);
    }

    public String getNumberRaw() {
        return number;
    }

    public BlockInfo(Block block, Admin admin) {
        super();
        @SuppressWarnings("rawtypes")
        List<TransactionResult> transactions = block.getTransactions();
        Long transCount = 0L;
        Long contractCount = 0L;
        // added for block info 20181229;
        BigInteger totalGasUsed = BigInteger.ZERO;
        BigInteger totalGas = BigInteger.ZERO;
        BigInteger totalGasSpent = BigInteger.ZERO;

/*		if(transactions!=null&&transactions.size()!=0) {
			for(TransactionResult<?> transactionResult:transactions){
				Transaction transaction = (Transaction) transactionResult.get();
				String hash2 = transaction.getHash();
                TransactionReceipt transactionReceipt = null;
				try {
                     transactionReceipt = admin.hpbGetTransactionReceipt(hash2).send().getResult();
					String contractAddress = transactionReceipt.getContractAddress();
					if(contractAddress!=null) {
						contractCount++;
					}else {
						transCount++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
                // added for block info 20181229;
                totalGasUsed = totalGasUsed.add(block.getGasUsed());
				totalGas = totalGas.add(transaction.getGas());
                totalGasSpent = totalGasSpent.add(transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()));
			};
		}*/


        //
        // added for block info 20181229;
        if (transCount > 0) {

 /*           this.blockGasUsed = String.valueOf(totalGasUsed) ;
            this.blockGas = String.valueOf(totalGas);
            this.blockGasSpent = String.valueOf(totalGasSpent);
            this.averageGasPrice = String.valueOf(totalGasSpent.divide(totalGas)) ;*/
        } else {
            this.averageGasPrice = String.valueOf(0);
            this.blockGasUsed = String.valueOf(0);
            this.blockGas = String.valueOf(0);
            this.blockGasSpent = String.valueOf(0);
        }
        this.transCount = transCount;
        this.contractCount = contractCount;
        this.number = block.getNumberRaw();
        this.hash = block.getHash();
        this.parentHash = block.getParentHash();
        this.nonce = block.getNonceRaw();
        this.sha3Uncles = block.getSha3Uncles();
        this.logsBloom = block.getLogsBloom();
        this.transactionsRoot = block.getTransactionsRoot();
        this.stateRoot = block.getStateRoot();
        this.receiptsRoot = block.getReceiptsRoot();
        this.author = block.getAuthor();
        this.miner = block.getMiner();
        this.mixHash = block.getMixHash();
        this.difficulty = block.getDifficultyRaw();
        this.totalDifficulty = block.getTotalDifficultyRaw();
        this.extraData = block.getExtraData();
        this.size = block.getSizeRaw();
        this.gasLimit = block.getGasLimitRaw();
        this.gasUsed = block.getGasUsedRaw();
        this.timestamp = block.getTimestampRaw();
        this.uncles = block.getUncles();
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public BigInteger getNonce() {
        return Numeric.decodeQuantity(nonce);
    }

    public String getNonceRaw() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSha3Uncles() {
        return sha3Uncles;
    }

    public void setSha3Uncles(String sha3Uncles) {
        this.sha3Uncles = sha3Uncles;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public void setLogsBloom(String logsBloom) {
        this.logsBloom = logsBloom;
    }

    public String getTransactionsRoot() {
        return transactionsRoot;
    }

    public void setTransactionsRoot(String transactionsRoot) {
        this.transactionsRoot = transactionsRoot;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getReceiptsRoot() {
        return receiptsRoot;
    }

    public void setReceiptsRoot(String receiptsRoot) {
        this.receiptsRoot = receiptsRoot;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public String getMixHash() {
        return mixHash;
    }

    public void setMixHash(String mixHash) {
        this.mixHash = mixHash;
    }

    public BigInteger getDifficulty() {
        return Numeric.decodeQuantity(difficulty);
    }

    public String getDifficultyRaw() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public BigInteger getTotalDifficulty() {
        return Numeric.decodeQuantity(totalDifficulty);
    }

    public String getTotalDifficultyRaw() {
        return totalDifficulty;
    }

    public void setTotalDifficulty(String totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public BigInteger getSize() {
        return Numeric.decodeQuantity(size);
    }

    public String getSizeRaw() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigInteger getGasLimit() {
        return Numeric.decodeQuantity(gasLimit);
    }

    public String getGasLimitRaw() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public BigInteger getGasUsed() {
        return Numeric.decodeQuantity(gasUsed);
    }

    public String getGasUsedRaw() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getAverageGasPrice() {
        return averageGasPrice;
    }

    public void setAverageGasPrice(String averageGasPrice) {
        this.averageGasPrice = averageGasPrice;
    }

    public BigInteger getTimestamp() {
        return Numeric.decodeQuantity(timestamp);
    }

    public String getTimestampRaw() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public List<String> getUncles() {
        return uncles;
    }

    public void setUncles(List<String> uncles) {
        this.uncles = uncles;
    }

    public String getBlockGasUsed() {
        return blockGasUsed;
    }

    public void setBlockGasUsed(String blockGasUsed) {
        this.blockGasUsed = blockGasUsed;
    }

    public String getBlockGas() {
        return blockGas;
    }

    public void setBlockGas(String blockGas) {
        this.blockGas = blockGas;
    }

    public String getBlockGasSpent() {
        return blockGasSpent;
    }

    public void setBlockGasSpent(String blockGasSpent) {
        this.blockGasSpent = blockGasSpent;
    }
}