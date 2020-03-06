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
