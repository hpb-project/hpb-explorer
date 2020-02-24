package com.hpb.bc.async;

import com.alibaba.fastjson.JSON;
import com.hpb.bc.constant.StringConstant;
import com.hpb.bc.entity.Erc721Token;
import com.hpb.bc.entity.TxTransferRecord;
import com.hpb.bc.util.ERC721Full;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@Component
public class AsyncErc721Task {

    private static final Logger logger = LoggerFactory.getLogger(AsyncErc721Task.class);

    @Autowired
    private Admin admin;

    @Async
    public void setBlockTimestamp(TxTransferRecord r, CountDownLatch latch) {
        String blockHash = r.getBlockHash();
        try {
            HpbBlock.Block block = admin.hpbGetBlockByHash(blockHash, false).send().getBlock();
            BigInteger timestamp = block.getTimestamp();
            r.setBlockTimestamp(timestamp.longValue());
        } catch (Exception e) {
            logger.error("获取交易所在块时间失败,当前转移记录对象为: \r\n {}", JSON.toJSONString(r));
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    @Async
    public void buildTokenListByIndex(String address, List<Erc721Token> tokenList, long tokenIndex, CountDownLatch latch) {
        try {
            Erc721Token token = new Erc721Token();
            token.setTokenIndex(BigInteger.valueOf(tokenIndex));
            token.setParentErc721Address(address);
            try {
                BigInteger tokenId = rpcGetTokenIdByIndex(tokenIndex, address);
                token.setTokenId(tokenId.longValue());
            } catch (Exception ex) {
                logger.error("查询合约地址为 {} 的tokenIndex 为 {} 失败,异常信息", address, tokenIndex, ex.getMessage());
                ex.printStackTrace();
            }
            tokenList.add(token);
        } catch (Throwable t) {
            logger.error("发生了未知异常:{}", t.getMessage());
        } finally {
            long count = latch.getCount();
            logger.debug("当前锁的count : {}", count);
            latch.countDown();
        }
    }

    private BigInteger rpcGetTokenIdByIndex(long tokenIndex, String address) throws IOException {
        final Function function = new Function(ERC721Full.FUNC_TOKENBYINDEX,
                Arrays.<Type>asList(new Uint256(tokenIndex)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        HpbCall hpbCall = null;
        hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(StringConstant.ZERO_ADDRESS, address, encodedFunction), DefaultBlockParameterName.LATEST).send();
        String response = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(response, function.getOutputParameters());
        Uint256 uint256 = (Uint256) typeList.get(0);
        return uint256.getValue();
    }

    @Async
    public void setHolderAddress(String address, Erc721Token token, Uint256 tokenId, CountDownLatch latch) {
        try {
            String holderAddress = rpcGetOwnerOfTokenById(tokenId, address);
            token.setHolderAddress(holderAddress);
        } catch (Exception ex) {
            logger.error("查询合约地址为 {},tokenId为 {} 的当前持有人失败,异常信息为:{}", address, token, ex.getMessage());
            ex.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    private String rpcGetOwnerOfTokenById(Uint256 tokenId, String address) throws IOException {
        final Function function = new Function(ERC721Full.FUNC_OWNEROF,
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(
                StringConstant.ZERO_ADDRESS, address, encodedFunction),
                DefaultBlockParameterName.LATEST).send();
        String response = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(response, function.getOutputParameters());
        Address responseAddress = (Address) typeList.get(0);

        return responseAddress.getValue();
    }


    @Async
    public void setUri(String contactAddress, Erc721Token e, Uint256 tokenId, CountDownLatch latch) {
        try {
            String tokenUri = rpcGetTokenUriByTokenId(tokenId, contactAddress);
            e.setImageUrl(tokenUri);
        } catch (Exception ex) {
            logger.error("查询合约地址为 {},tokenId为 {} 的图片uri失败,异常信息为:{}", contactAddress, e, ex.getMessage());
            ex.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    private String rpcGetTokenUriByTokenId(Uint256 tokenId, String contactAddress) throws IOException {
        final Function function = new Function(ERC721Full.FUNC_TOKENURI,
                Arrays.<Type>asList(tokenId),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        io.hpb.web3.protocol.core.methods.response.HpbCall hpbCall = admin.hpbCall(
                Transaction.createHpbCallTransaction(
                        StringConstant.ZERO_ADDRESS, contactAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();

        String response = hpbCall.getValue();
        List<Type> typeList = FunctionReturnDecoder.decode(response, function.getOutputParameters());
        Utf8String utf8String = (Utf8String) typeList.get(0);

        return utf8String.toString();
    }

}
