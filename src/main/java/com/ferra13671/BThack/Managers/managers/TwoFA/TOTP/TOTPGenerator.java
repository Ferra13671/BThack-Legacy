package com.ferra13671.BThack.managers.managers.TwoFA.TOTP;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class TOTPGenerator extends OTPGenerator {
    private final Clock clock = Clock.systemUTC();

    public TOTPGenerator() {
        super(OTPLength.SIX, HMACDigest.SHA1);
    }

    public List<TOTP> generate(TotpSecret totpSecret) {
        return generate(totpSecret, 0, 0);
    }

    public List<TOTP> generate(TotpSecret totpSecret, int delaySteps, int futureSteps) {
        long step = ((clock.millis() / 1000)) / 30;
        List<TOTP> totpList = new ArrayList<>();

        for (long i = step - delaySteps; i <= step + futureSteps; i++) {
            HOTP hotpValue = generateOtp(totpSecret.value, i);
            totpList.add(new TOTP(hotpValue.value()));
        }

        return totpList;
    }

    public TOTP generateCurrent(TotpSecret totpSecret) {
        return generate(totpSecret).getFirst();
    }
}
