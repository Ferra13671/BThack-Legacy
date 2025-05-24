package com.ferra13671.BThack.managers.managers.TwoFA.TOTP;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class OTPGenerator {
    protected final OTPLength otpLength;
    protected final HMACDigest digest;
    private final int modulusOperand;

    protected OTPGenerator(OTPLength otpLength, HMACDigest digest) {
        this.otpLength = otpLength;
        this.digest = digest;
        this.modulusOperand = (int) Math.pow(10, otpLength.getValue());
    }

    protected HOTP generateOtp(byte[] key, long counter) {
        byte[] hmacOut = HmacUtils.getInitializedMac(digest.value, key).doFinal(toByteArray(counter));
        int intOtp = truncate(hmacOut) % modulusOperand;
        return new HOTP(String.format("%0" + otpLength.getValue() + "d", intOtp));
    }

    private int truncate(byte[] hmac) {
        int offset = hmac[hmac.length - 1] & 0xf;
        byte[] slice = Arrays.copyOfRange(hmac, offset, offset + 4);
        slice[0] = (byte) (slice[0] & 0x7f);
        return ByteBuffer.wrap(slice).getInt();
    }

    private static byte[] toByteArray(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, value);
        return buffer.array();
    }

    public enum HMACDigest {
        SHA1(HmacAlgorithms.HMAC_SHA_1),
        SHA256(HmacAlgorithms.HMAC_SHA_256),
        SHA512(HmacAlgorithms.HMAC_SHA_512);

        public final HmacAlgorithms value;

        HMACDigest(HmacAlgorithms value) {
            this.value = value;
        }
    }

    public enum OTPLength {
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10);

        private final int value;

        OTPLength(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

