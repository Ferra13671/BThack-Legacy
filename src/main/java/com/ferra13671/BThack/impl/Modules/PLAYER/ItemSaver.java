package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.core.Render.BThackRender;
import com.ferra13671.BThack.core.Render.Font.FontUtils;
import com.ferra13671.BThack.core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Events.Block.AttackBlockEvent;
import com.ferra13671.BThack.api.Events.Block.UseBlockEvent;
import com.ferra13671.BThack.api.Events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.api.Events.Render.RenderHudPreEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.Event;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class ItemSaver extends Module {

    public final NumberSetting minStrength = new NumberSetting("Min Strength(%)", this, 5, 1, 90, false);
    public final BooleanSetting attackSaver = new BooleanSetting("Attack Saver", this, true);

    public ItemSaver() {
        super("ItemSaver",
                "lang.module.ItemSaver",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }

    private int alpha = 0;

    @Override
    public void onChangeSetting(Setting<?> setting) {
        arrayListInfo = minStrength.getValue() + "%";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        arrayListInfo = minStrength.getValue() + "%";
    }

    @EventSubscriber
    public void onRenderOverlay(RenderHudPreEvent e) {
        if (alpha > 0) {
            BThackRender.drawString(LanguageSystem.translate("lang.module.ItemSaver.saveMessage"), (mc.getWindow().getScaledWidth() / 2f) - (FontUtils.getTextWidth(LanguageSystem.translate("lang.module.ItemSaver.saveMessage")) / 2f), (mc.getWindow().getScaledHeight() / 2f) + 40, ColorUtils.fastRGBA(255, 98, 0, Math.min(Math.max(alpha, 1), 255)));
        }
        if (alpha > 0) alpha--;
    }

    @EventSubscriber
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck()) return;

        check(e);
    }

    @EventSubscriber
    public void onUseBlock(UseBlockEvent e) {
        if (nullCheck()) return;

        check(e);
    }

    @EventSubscriber
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (attackSaver.getValue()) check(e);
    }


    private void check(Event e) {
        ItemStack item = InventoryUtils.getItem(mc.player.getInventory().selectedSlot);
        if (item.getItem() instanceof BlockItem) return;
        float currentDamage = ItemUtils.getItemDurabilityInPercentages(item);
        if (currentDamage < minStrength.getValue()) {
            if (alpha != 380) alpha = 380;
            e.setCancelled(true);
        }
    }
}
