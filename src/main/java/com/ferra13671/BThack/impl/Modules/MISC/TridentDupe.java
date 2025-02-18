package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Events.DisconnectEvent;
import com.ferra13671.BThack.api.Events.GuiOpenEvent;
import com.ferra13671.BThack.api.Events.PacketEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;

public class TridentDupe extends Module {

    public final NumberSetting delay = new NumberSetting("Delay", this, 9, 1, 20, true);
    public final BooleanSetting dropTridents = new BooleanSetting("Drop Tridents", this, false);

    public TridentDupe() {
        super("TridentDupe",
                "lang.module.TridentDupe",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );

        initSettings(
                delay,
                dropTridents
        );
    }

    @EventSubscriber(priority = Integer.MAX_VALUE)
    private void onSendPacket(PacketEvent.Send event) {

        if (event.getPacket() instanceof PlayerMoveC2SPacket
                || event.getPacket() instanceof CloseHandledScreenC2SPacket)
            return;

        if (!(event.getPacket() instanceof ClickSlotC2SPacket)
                && !(event.getPacket() instanceof PlayerActionC2SPacket))
        {
            return;
        }
        if (!cancel)
            return;

        MutableText packetStr = Text.literal(event.getPacket().toString()).formatted(Formatting.WHITE);

        event.cancel();
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        super.onEnable();

        for (int i = 0; i < 9; i++)
        {
            if (mc.player.getInventory().getStack((i)).getItem() == Items.TRIDENT)
            {
                Integer currentHotbarDamage = mc.player.getInventory().getStack((i)).getDamage();

            }
        }

        PlayerInteractItemC2SPacket pckt = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 10, -57.0f, 66.29f);

        Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap<>();

        modifiedStacks.put(3,  mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot));
        modifiedStacks.put(36,  mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot));

        ClickSlotC2SPacket packet = new ClickSlotC2SPacket(0, 15, 0, 0, SlotActionType.SWAP,
                new ItemStack(Items.AIR), modifiedStacks);

        scheduledTasks.clear();
        dupe();

    }

    private void dupe()
    {
        int delayInt = (int) delay.getValue() * 100;

        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        cancel = true;
        scheduleTask(() -> {
            cancel = false;

            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 3, 0, SlotActionType.SWAP, mc.player);

            PlayerActionC2SPacket packet2 = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN, 0);
            mc.getNetworkHandler().sendPacket(packet2);

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
    public void onTick(ClientTickEvent e) {
        long currentTime = System.currentTimeMillis();
        {
            Iterator<Pair<Long, Runnable>> iterator = scheduledTasks.iterator();

            while (iterator.hasNext()) {
                Pair<Long, Runnable> entry = iterator.next();
                if (entry.getLeft() <= currentTime) {
                    entry.getRight().run();
                    iterator.remove(); // Remove executed task from the list
                }
            }
        }
        {
            Iterator<Pair<Long, Runnable>> iterator = scheduledTasks2.iterator();

            while (iterator.hasNext()) {
                Pair<Long, Runnable> entry = iterator.next();
                if (entry.getLeft() <= currentTime) {
                    entry.getRight().run();
                    iterator.remove(); // Remove executed task from the list
                }
            }
        }
    }

    @EventSubscriber
    public void onDisconnect(DisconnectEvent e) {
        toggle();
    }

    @EventSubscriber
    public void onGui(GuiOpenEvent e) {
        if (e.getScreen() instanceof DisconnectedScreen) {
            setToggled(false);
        }
    }
}
