package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import org.apache.commons.lang3.math.NumberUtils;

public class RotateCommand extends AbstractCommand {

    public RotateCommand() {
        super("lang.command.Rotate.description", "rotate [yaw{-30000 : 30000}] [pitch{-90 : 90}]", "r" + "rotate"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 2 && NumberUtils.isNumber(args[0]) && NumberUtils.isNumber(args[1])) {
            double yaw = Double.parseDouble(args[0]);
            double pitch = Double.parseDouble(args[1]);

            if (isCorrectRotations(yaw, pitch) && mc.player != null) {

                mc.player.setYaw((float) yaw);
                mc.player.setPitch((float) pitch);
            } else {
                invalidArgumentError();
            }
        } else {
            invalidArgumentError();
        }
    }

    private boolean isCorrectRotations(double yaw, double pitch) {
        return yaw >= -30000 && yaw <= 30000 && pitch >= -90 && pitch <= 90;
    }
}
