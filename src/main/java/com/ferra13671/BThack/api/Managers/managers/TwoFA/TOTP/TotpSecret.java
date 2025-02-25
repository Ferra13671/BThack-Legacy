package com.ferra13671.BThack.api.Managers.managers.TwoFA.TOTP;

import org.apache.commons.codec.binary.Base32;

import java.util.Arrays;

public class TotpSecret {
    public final byte[] value;
    public final String base32Encoded;


    public TotpSecret(byte[] value) {
        this.value = value;
        base32Encoded = new Base32().encodeToString(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TotpSecret totpSecret)) return false;
        return Arrays.equals(value, totpSecret.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    public static TotpSecret fromBase32EncodedString(String value) {
        return new TotpSecret(new Base32().decode(value));
    }
}
