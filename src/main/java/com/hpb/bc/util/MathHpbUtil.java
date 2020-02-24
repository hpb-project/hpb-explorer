package com.hpb.bc.util;

import io.hpb.web3.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathHpbUtil {

    public static String ratePercent(BigInteger numeratorNumber, BigInteger denominatorNumber) {
        try {
            BigDecimal numeratorNumberInHpb = Convert.fromWei(String.valueOf(numeratorNumber), Convert.Unit.HPB);
            BigDecimal denominatorNumberInHpb = Convert.fromWei(String.valueOf(denominatorNumber), Convert.Unit.HPB);
            double r = numeratorNumberInHpb.doubleValue() / (denominatorNumberInHpb.doubleValue());
            String rp = new BigDecimal(r * 100).setScale(4, BigDecimal.ROUND_HALF_EVEN).toPlainString();
            return rp + "%";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ;
}
