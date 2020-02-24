package com.hpb.bc.util;

import com.alibaba.fastjson.JSON;
import com.hpb.bc.constant.StringConstant;
import com.hpb.bc.model.ContractErcProfileSummaryModel;
import com.hpb.bc.solidity.SolidityCompiler;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.*;
import io.hpb.web3.abi.datatypes.generated.Bytes4;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbCall;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.http.HttpService;
import io.hpb.web3.protocol.ipc.UnixIpcService;
import io.hpb.web3.protocol.ipc.WindowsIpcService;
import io.hpb.web3.tx.ChainId;
import io.hpb.web3.tx.RawTransactionManager;
import io.hpb.web3.utils.Convert;
import io.hpb.web3.utils.Numeric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lij <email=jian.li@hpb.io>
 * @CreatTime 2019/9/4 20:36
 * @Desc 类说明
 */
@Component
public class Erc721Helper {

    @Autowired
    private Admin admin;


    private static final Logger logger = LoggerFactory.getLogger(Erc721Helper.class);

    private static final BigInteger GAS_PRICE = Convert.toWei("18", Convert.Unit.GWEI).toBigInteger();
    private static final BigInteger GAS_LIMIT = new BigInteger("90000000");
    private Credentials credentials = Credentials.create("C047F58CA88175D20051A36E2D1269F90EFBE0B294D01474135B199C52AC21F1");
//    private Credentials credentials = Credentials.create("277C19C4C87003170F4EC518B028EE1BA55B819486B6AD07DC004D7D0A1619C7");
    RawTransactionManager transactionManager = new RawTransactionManager(getAdmin(BLOCK_CHAIN_PRO_URL), credentials, ChainId.MAINNET_269);

    public HeroAsset getHeroAsset(String contractAddress) {
        return HeroAsset.load(contractAddress, getAdmin(BLOCK_CHAIN_PRO_URL), transactionManager, GAS_PRICE, GAS_LIMIT);
    }

    public ERC721Full getERC721Full(String contractAddress) {
        return ERC721Full.load(contractAddress, getAdmin(BLOCK_CHAIN_PRO_URL), transactionManager, GAS_PRICE, GAS_LIMIT);
    }

    private static String frontMessage = "woshilijian";

    private static final String PRIVATE_KEY = "C047F58CA88175D20051A36E2D1269F90EFBE0B294D01474135B199C52AC21F1";
    public static final String PUBLIC_KEY = "EfE4C938dd600488125B671D78D42eadD57B21bc";

    public static void main(String[] args) throws Exception {
        String sign = Sign.sign("34ced64c5136228f6b60724668e3d304", "277C19C4C87003170F4EC518B028EE1BA55B819486B6AD07DC004D7D0A1619C7");
        System.out.println("sign = " + sign);
        HeroAsset heroAsset = new Erc721Helper().getHeroAsset("0x721fe25eae4948051b213f8fd126da0f76974819");

//        heroAsset.setTokenURIPrefix(new Utf8String("https://img.cryptokitties.co/0x06012c8cf97bead5deae237070f9587f8e7a266d/1.svg?a=")).send();

//        Address from = new Address("0xEfE4C938dd600488125B671D78D42eadD57B21bc");
//        Address to = new Address("0x721fe25eae4948051b213f8fd126da0f76974819");
//
//        String status = heroAsset.safeTransferFrom(from, to, new Uint256(0L)).send().getStatus();
//        System.out.println("status = " + status);

//        ContractErcProfileSummaryModel.ContractErcProfileSummaryInner contractErcProfileSummaryInner = new ContractErcProfileSummaryModel.ContractErcProfileSummaryInner();
//        contractErcProfileSummaryInner.setProfileZh("111111");
//        contractErcProfileSummaryInner.setProfileEn("111111");
//        contractErcProfileSummaryInner.setEmail("111111");
//        contractErcProfileSummaryInner.setFacebook("111111");
//        contractErcProfileSummaryInner.setTwitter("111111");
//        contractErcProfileSummaryInner.setWeibo("111111");
//        contractErcProfileSummaryInner.setGithub("111111");
//        contractErcProfileSummaryInner.setTokenSymbolImageUrl("111111");
//        contractErcProfileSummaryInner.setTokenName("111111");
//        contractErcProfileSummaryInner.setContractAddress("111111");
//        contractErcProfileSummaryInner.setContractType("111111");
//        contractErcProfileSummaryInner.setOfficialSite("111111");
//
//
//        String s1 = JSON.toJSONString(contractErcProfileSummaryInner);
//        System.out.println("s1 = " + s1);

        try {
//            String s = mkMd5Msg(frontMessage);


//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//            keyPairGenerator.initialize(256);
//
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
//            ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
//
////            ECPublicKeyImpl ecPublicKey1 = new ECPublicKeyImpl();
//
//
//            // 2.执行签名
//            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
//            KeyFactory keyFactory = KeyFactory.getInstance("EC");
//
//            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//            Signature signature = Signature.getInstance("SHA1withECDSA");
//            signature.initSign(privateKey);
//
//            signature.updateMoreInfo(frontMessage.getBytes());
//            byte[] sign = signature.sign();
//
//            // 验证签名
//            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
//            keyFactory = KeyFactory.getInstance("EC");
//            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
//            signature = Signature.getInstance("SHA1withECDSA");
//            signature.initVerify(publicKey);
//            signature.updateMoreInfo(frontMessage.getBytes());
//
//            boolean bool = signature.verify(sign);
//            System.out.println(bool);


            ERC721Full erc721Full33 = new Erc721Helper().getERC721Full("0x721fe25eae4948051b213f8fd126da0f76974819");
//
//            Bool send1 = erc721Full33.supportsInterface(new Bytes4(Numeric.hexStringToByteArray("0x780e9d63"))).send();
//            System.out.println("send1 = " + send1.getValue());
//
//            String value = erc721Full33.symbol().send().getValue();
//            System.out.println("value = " + value);
//            //
//            StopWatch stopWatch = new StopWatch();
//
//            stopWatch.start();
//
//            final Function function = new Function("tokenByIndex",
//                    Arrays.<Type>asList(new Uint256(2176)),
//                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
//                    }));
//            String encodedFunction = FunctionEncoder.encode(function);
//            io.hpb.web3.protocol.core.methods.response.HpbCall hpbCall = getAdmin(BLOCK_CHAIN_PRO_URL).hpbCall(
//                    Transaction.createHpbCallTransaction("0x07a05bd38b42b4d390a309cd6505d64a20a3655a"
//                            , "0xba29fa851805767609c25b7a032101cef1c2258b", encodedFunction), DefaultBlockParameterName.LATEST
//            )
//                    .send();
//            String value2 = hpbCall.getValue();
//            List<Type> decode = FunctionReturnDecoder.decode(value2, function.getOutputParameters());
//            Uint256 uint256 = (Uint256) decode.get(0);
//
//            stopWatch.stop();
//            long totalTimeMillis1 = stopWatch.getTotalTimeMillis();
//
//            StopWatch stopWatch2 = new StopWatch();
//
//            stopWatch2.start();
//            ERC721Full erc721Full = new Erc721Helper().getERC721Full("0xba29fa851805767609c25b7a032101cef1c2258b");
//            Uint256 send = erc721Full.tokenByIndex(new Uint256(2176)).send();
//            stopWatch2.stop();
//
//            long totalTimeMillis2 = stopWatch2.getTotalTimeMillis();
//
//            System.out.println("stopWatch.getTotalTimeMillis()1111 = " + totalTimeMillis1);
//            System.out.println("stopWatch =222222 " + totalTimeMillis2);
//            System.out.println("时间差" + (totalTimeMillis2-totalTimeMillis1));
//
//            Address fromAddress = new Address("0x784c9d2190462BA2E2af8071003BBD3A498D9259");
//            Address toAddress = new Address("0xEfE4C938dd600488125B671D78D42eadD57B21bc");
//
            Address fromAddress = new Address("0xEfE4C938dd600488125B671D78D42eadD57B21bc");
            Address toAddress = new Address("0x721fe25eae4948051b213f8fd126da0f76974819");
//
            TransactionReceipt send = erc721Full33.safeTransferFrom(fromAddress, toAddress, new Uint256(0L)).send();
            System.out.println("send = " + send.getStatus());
//
//            String value = erc721Full33.ownerOf(new Uint256(0L)).send().getValue();
//            System.out.println("value = " + value);
//            String value1 = erc721Full33.ownerOf(new Uint256(1L)).send().getValue();
//            System.out.println("value1 = " + value1);

//            Boolean value2 = erc721Full33.supportsInterface(new Bytes4(Numeric.hexStringToByteArray(StringConstant.ERC_721_METHOD_BYTE_STRING))).send().getValue();
//            System.out.println("value2 = " + value2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String mkMd5Msg(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(msg.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把没一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static final String BLOCK_CHAIN_PRO_URL = "http://mainnet.hpb.io/";
    public static final String BLOCK_CHAIN_TEST_URL = "http://testnet.hpb.io/";

    public static Admin getAdmin(String environment) {
        return Admin.build(buildService(environment));
    }

    private static Web3Service buildService(String clientAddress) {
        Web3Service Web3Service;
        if (clientAddress == null || clientAddress.equals("")) {
            Web3Service = new HttpService(createOkHttpClient());
        } else if (clientAddress.startsWith("http")) {
            Web3Service = new HttpService(clientAddress, createOkHttpClient(), false);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            Web3Service = new WindowsIpcService(clientAddress);
        } else {
            Web3Service = new UnixIpcService(clientAddress);
        }

        return Web3Service;
    }

    public boolean isHRC721Contact(String contractAddress) {
        try {
            final Function function = new Function(ERC721Full.FUNC_SUPPORTSINTERFACE,
                    Arrays.<Type>asList(new Bytes4(Numeric.hexStringToByteArray(StringConstant.ERC_721_METHOD_BYTE_STRING))),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                    }));

            String encodedFunction = FunctionEncoder.encode(function);
            HpbCall hpbCall = admin.hpbCall(Transaction.createHpbCallTransaction(StringConstant.ZERO_ADDRESS, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
            String value = hpbCall.getValue();
            List<Type> typeList = FunctionReturnDecoder.decode(value, function.getOutputParameters());
            Bool bool = (Bool) typeList.get(0);
            return bool.getValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (logger.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    private static void configureTimeouts(OkHttpClient.Builder builder) {
        int WEB3J_TIMEOUT = 800;
        builder.connectTimeout(WEB3J_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(WEB3J_TIMEOUT, TimeUnit.SECONDS); // Sets the socket timeout too
        builder.writeTimeout(WEB3J_TIMEOUT, TimeUnit.SECONDS);
    }


}
