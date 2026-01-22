package com.ferra13671.BThack.impl.modules.misc;

import com.ferra13671.BThack.events.ClientTickEvent;
import com.ferra13671.BThack.events.DisconnectEvent;
import com.ferra13671.BThack.events.GuiOpenEvent;
import com.ferra13671.BThack.events.PacketEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.Setting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.api.utils.PlayerUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;

@ModuleInfo(name = "TridentDupe", description = "lang.module.TridentDupe", category = "MISC")
public class TridentDupe extends Module {

    public final NumberSetting delay = new NumberSetting("Delay", this, 9, 1, 20, true);
    public final BooleanSetting dropTridents = new BooleanSetting("Drop Tridents", this, false);

    public final BooleanSetting autoInventory = new BooleanSetting("Auto Inventory", this, true);

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onChangeSetting(Setting<?> setting) {
        if (isEnabled()) {
            if (autoInventory.getValue()) {
                if (mc.currentScreen != null) PlayerUtils.closeHandledScreen();
                mc.setScreen(new InventoryScreen(mc.player));
            }
        }
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    @SuppressWarnings("unused")
    public void onSendPacket(PacketEvent.Send event) {

        if (event.getPacket() instanceof PlayerMoveC2SPacket
                || event.getPacket() instanceof CloseHandledScreenC2SPacket)
            return;

        if (!(event.getPacket() instanceof ClickSlotC2SPacket)
                && !(event.getPacket() instanceof PlayerActionC2SPacket))
            return;

        if (!cancel)
            return;

        event.cancel();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        if (autoInventory.getValue()) {
            if (mc.currentScreen != null) mc.setScreen(null);
            mc.setScreen(new InventoryScreen(mc.player));
        }

        super.onEnable();

        scheduledTasks.clear();
        dupe();

    }

    @SuppressWarnings("DataFlowIssue")
    private void dupe() {
        int delayInt = delay.getValue().intValue() * 100;

        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        cancel = true;
        scheduleTask(() -> {
            cancel = false;

            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 3, 0, SlotActionType.SWAP, mc.player);

            PlayerActionC2SPacket packet2 = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN, 0);
            Managers.NETWORK_MANAGER.sendPacket(packet2);

            if(dropTridents.getValue()) mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 44, 0, SlotActionType.THROW, mc.player);

            cancel = true;
            scheduleTask2(this::dupe, delayInt);
        }, delayInt);
    }


    private boolean cancel = true;

    private final List<Pair<Long, Runnable>> scheduledTasks = new ArrayList<>();
    private final List<Pair<Long, Runnable>> scheduledTasks2 = new ArrayList<>();

    public void scheduleTask(Runnable task, long delayMillis) {
        long executeTime = System.currentTimeMillis() + delayMillis;
        scheduledTasks.add(new Pair<>(executeTime, task));
    }
    public void scheduleTask2(Runnable task, long delayMillis) {
        long executeTime = System.currentTimeMillis() + delayMillis;
        scheduledTasks2.add(new Pair<>(executeTime, task));
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onTick(ClientTickEvent e) {

        long currentTime = System.currentTimeMillis();
        Iterator<Pair<Long, Runnable>> iterator = scheduledTasks.iterator();

        while (iterator.hasNext()) {
            Pair<Long, Runnable> entry = iterator.next();
            if (entry.getLeft() <= currentTime) {
                entry.getRight().run();
                iterator.remove(); // Remove executed task from the list
            }
        }
        iterator = scheduledTasks2.iterator();
        while (iterator.hasNext()) {
            Pair<Long, Runnable> entry = iterator.next();
            if (entry.getLeft() <= currentTime) {
                entry.getRight().run();
                iterator.remove(); // Remove executed task from the list
            }
        }
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onDisconnect(DisconnectEvent e) {
        toggle();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onGui(GuiOpenEvent e) {
        if (autoInventory.getValue() && (e.getScreen() == null || !(e.getScreen() instanceof InventoryScreen))) setEnabled(false);
        if (e.getScreen() instanceof DisconnectedScreen)
            setEnabled(false);
    }
}
