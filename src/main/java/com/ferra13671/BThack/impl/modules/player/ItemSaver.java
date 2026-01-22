package com.ferra13671.BThack.impl.modules.player;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.render.BThackRender;
import com.ferra13671.BThack.core.render.font.FontUtils;
import com.ferra13671.BThack.core.render.utils.ColorUtils;
import com.ferra13671.BThack.events.block.AttackBlockEvent;
import com.ferra13671.BThack.events.block.UseBlockEvent;
import com.ferra13671.BThack.events.entity.AttackEntityEvent;
import com.ferra13671.BThack.events.render.RenderHudPreEvent;
import com.ferra13671.BThack.managers.impl.setting.Settings.BooleanSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.NumberSetting;
import com.ferra13671.BThack.managers.impl.setting.Settings.Setting;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.InventoryUtils;
import com.ferra13671.BThack.api.utils.ItemUtils;
import com.ferra13671.MegaEvents.Base.Event;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "ItemSaver", description = "lang.module.ItemSaver", category = "PLAYER")
public class ItemSaver extends Module {

    public final NumberSetting minStrength = new NumberSetting("Min Strength(%)", this, 5, 1, 90, false);
    public final BooleanSetting attackSaver = new BooleanSetting("Attack Saver", this, true);

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
    @SuppressWarnings("unused")
    public void onRenderOverlay(RenderHudPreEvent e) {
        if (alpha > 0)
            BThackRender.drawString(LanguageSystem.translate("lang.module.ItemSaver.saveMessage"), (mc.getWindow().getScaledWidth() / 2f) - (FontUtils.getTextWidth(LanguageSystem.translate("lang.module.ItemSaver.saveMessage")) / 2f), (mc.getWindow().getScaledHeight() / 2f) + 40, ColorUtils.fastRGBA(255, 98, 0, Math.min(Math.max(alpha, 1), 255)));
        if (alpha > 0) alpha--;
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onAttackBlock(AttackBlockEvent e) {
        if (nullCheck()) return;

        check(e);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onUseBlock(UseBlockEvent e) {
        if (nullCheck()) return;

        check(e);
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (attackSaver.getValue()) check(e);
    }

    @SuppressWarnings("DataFlowIssue")
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
