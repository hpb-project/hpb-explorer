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

import io.hpb.web3.crypto.ECDSASignature;
import io.hpb.web3.crypto.ECKeyPair;
import io.hpb.web3.crypto.Hash;
import io.hpb.web3.crypto.Keys;
import io.hpb.web3.rlp.*;
import io.hpb.web3.utils.Bytes;
import io.hpb.web3.utils.Numeric;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.hpb.web3.utils.Assertions.verifyPrecondition;


public class Sign {

    public static String sign(String message, String privateKey) {
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        SignatureData signatureData = signMessage(message.getBytes(StandardCharsets.UTF_8), ecKeyPair, false);
        List<RlpType> values = new ArrayList<>();
        values.add(RlpString.create(message));
        values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getV())));
        values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
        values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        RlpList rlpList = new RlpList(values);
        String signMessage = Numeric.toHexString(RlpEncoder.encode(rlpList));
        return signMessage;
    }

    public static SignatureData signMessage(byte[] message, ECKeyPair keyPair, boolean needToHash) {
        BigInteger publicKey = keyPair.getPublicKey();
        byte[] messageHash;
        if (needToHash) {
            messageHash = Hash.sha3(message);
        } else {
            messageHash = message;
        }

        ECDSASignature sig = keyPair.sign(messageHash);

        int recId = -1;
        for (int i = 0; i < 4; i++) {
            BigInteger k = recoverFromSignature(i, sig, messageHash);
            if (k != null && k.equals(publicKey)) {
                recId = i;
                break;
            }
        }
        if (recId == -1) {
            throw new RuntimeException(
                    "Could not construct a recoverable key. Are your credentials valid?");
        }

        int headerByte = recId + 27;


        byte[] v = new byte[]{(byte) headerByte};
        byte[] r = Numeric.toBytesPadded(sig.r, 32);
        byte[] s = Numeric.toBytesPadded(sig.s, 32);

        return new SignatureData(v, r, s);
    }

    public static BigInteger recoverFromSignature(int recId, ECDSASignature sig, byte[] message) {
        byte[] qBytes = recoverPubBytesFromSignature(recId, sig, message);
        return new BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.length));
    }

    public static boolean verify(String signMessage, String address) throws SignatureException {
        byte[] message = Numeric.hexStringToByteArray(signMessage);
        RlpList returnList = RlpDecoder.decode(message);
        RlpList rlpList = (RlpList) returnList.getValues().get(0);
        byte[] msg = ((RlpString) rlpList.getValues().get(0)).getBytes();
        byte[] v = ((RlpString) rlpList.getValues().get(1)).getBytes();
        byte[] r = Numeric.toBytesPadded(
                Numeric.toBigInt(((RlpString) rlpList.getValues().get(2)).getBytes()), 32);
        byte[] s = Numeric.toBytesPadded(
                Numeric.toBigInt(((RlpString) rlpList.getValues().get(3)).getBytes()), 32);
        SignatureData signatureData = new SignatureData(v, r, s);
        verifyPrecondition(r != null && r.length == 32, "r must be 32 bytes");
        verifyPrecondition(s != null && s.length == 32, "s must be 32 bytes");
        int header = signatureData.getV()[0] & 0xFF;
        if (header < 27 || header > 34) {
            throw new SignatureException("Header byte out of range: " + header);
        }
        ECDSASignature signature = new ECDSASignature(
                new BigInteger(1, signatureData.getR()),
                new BigInteger(1, signatureData.getS()));
        int recId = header - 27;
        byte[] key = recoverPubBytesFromSignature(recId, signature, msg);
        BigInteger publicKey = new BigInteger(1, Arrays.copyOfRange(key, 1, key.length));
        if (address.equalsIgnoreCase(Keys.getAddress(publicKey))) {
            return verify(msg, signature, key);
        }
        return false;
    }

    public static boolean verify(byte[] data, ECDSASignature signature, byte[] pub) {
        ECDSASigner signer = new ECDSASigner();
        ECPublicKeyParameters params = new ECPublicKeyParameters(CURVE.getCurve().decodePoint(pub), CURVE);
        signer.init(false, params);
        try {
            return signer.verifySignature(data, signature.r, signature.s);
        } catch (NullPointerException npe) {
            return false;
        }
    }

    public static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    static final ECDomainParameters CURVE = new ECDomainParameters(
            CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
    static final BigInteger HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);

    static final String MESSAGE_PREFIX = "\u0019Hpb Signed Message:\n";

    private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte) (yBit ? 0x03 : 0x02);
        return CURVE.getCurve().decodePoint(compEnc);
    }

    public static byte[] recoverPubBytesFromSignature(int recId, ECDSASignature sig, byte[] message) {
        verifyPrecondition(recId >= 0, "recId must be positive");
        verifyPrecondition(sig.r.signum() >= 0, "r must be positive");
        verifyPrecondition(sig.s.signum() >= 0, "s must be positive");
        verifyPrecondition(message != null, "message cannot be null");
        BigInteger n = CURVE.getN();
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = sig.r.add(i.multiply(n));
        BigInteger prime = SecP256K1Curve.q;
        if (x.compareTo(prime) >= 0) {

            return null;
        }

        ECPoint R = decompressKey(x, (recId & 1) == 1);
        if (!R.multiply(n).isInfinity()) {
            return null;
        }

        BigInteger e = new BigInteger(1, message);
        BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
        BigInteger rInv = sig.r.modInverse(n);
        BigInteger srInv = rInv.multiply(sig.s).mod(n);
        BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
        ECPoint q = ECAlgorithms.sumOfTwoMultiplies(CURVE.getG(), eInvrInv, R, srInv);
        return q.getEncoded(false);
    }

    public static class SignatureData {
        private final byte[] v;
        private final byte[] r;
        private final byte[] s;

        public SignatureData(byte v, byte[] r, byte[] s) {
            this(new byte[]{v}, r, s);
        }

        public SignatureData(byte[] v, byte[] r, byte[] s) {
            this.v = v;
            this.r = r;
            this.s = s;
        }

        public byte[] getV() {
            return v;
        }

        public byte[] getR() {
            return r;
        }

        public byte[] getS() {
            return s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            SignatureData that = (SignatureData) o;

            if (!Arrays.equals(v, that.v)) {
                return false;
            }
            if (!Arrays.equals(r, that.r)) {
                return false;
            }
            return Arrays.equals(s, that.s);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(v);
            result = 31 * result + Arrays.hashCode(r);
            result = 31 * result + Arrays.hashCode(s);
            return result;
        }
    }
}
