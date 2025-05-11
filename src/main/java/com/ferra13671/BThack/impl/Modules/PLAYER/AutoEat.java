package com.ferra13671.BThack.impl.Modules.PLAYER;

import com.ferra13671.BThack.core.Client.ModuleList;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Hand;

public class AutoEat extends Module {

    public final NumberSetting startFoodLevel = new NumberSetting("Start FoodL", this, 15, 6, 19, false);
    public final BooleanSetting allowChorus = new BooleanSetting("Allow Chorus", this, false);
    public final BooleanSetting allowGapples = new BooleanSetting("Allow Gapples", this, false);

    public final BooleanSetting hpRegen = new BooleanSetting("HP Regen", this, false);
    public final NumberSetting startHP = new NumberSetting("Start HP", this, 15, 5, 19, false, hpRegen::getValue);

    public final BooleanSetting pauseIfMine = new BooleanSetting("Pause If Mine", this, true);


    public AutoEat() {
        super("AutoEat",
                "lang.module.AutoEat",
                KeyboardUtils.RELEASE,
                MCategory.PLAYER,
                false
        );
    }

    private boolean foodEating = false;
    private boolean gappleEating = false;

    @Override
    public void onEnable() {
        super.onEnable();
        foodEating = false;
        gappleEating = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        foodEating = false;
        gappleEating = false;
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        onAutoEat();
    }

    public void onAutoEat() {
        if (pauseIfMine.getValue())
            if (( mc.player.getActiveItem().getItem() instanceof ToolItem && mc.player.isUsingItem()) || (ModuleList.packetMine.isEnabled() && (ModuleList.packetMine.currentBreakingBlock != null || !ModuleList.packetMine.conveyorBlocks.isEmpty()))) return;
        if (mc.player.getHealth() <= startHP.getValue() && hpRegen.getValue()) {
            if (!isGolderApple(mc.player.getMainHandStack())) {
                for (int i = 0; i < 36; i++) {
                    if (isGolderApple(mc.player.getInventory().getStack(i))) {
                        if (i < 9) {
                            InventoryUtils.swapItem(i);
                            gappleEating = true;
                        } else {
                            int freeSlot = InventoryUtils.findFreeHotbarSlot();
                            if (freeSlot != -1) {
                                InventoryUtils.swapItemOnInventory(freeSlot, i);
                                InventoryUtils.swapItem(freeSlot);
                                gappleEating = true;
                            }
                            if (!gappleEating)
                                InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, i);
                        }
                        break;
                    }
                }
            } else {
                mc.options.useKey.setPressed(true);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                gappleEating = true;
            }
        } else {
            if (gappleEating) {
                mc.options.useKey.setPressed(KeyboardUtils.isKeyDown(mc.options.useKey.getDefaultKey().getCode()));
                gappleEating = false;
            }
        }

        if (!gappleEating) {
            if (mc.player.getHungerManager().getFoodLevel() <= startFoodLevel.getValue()) {
                ItemStack mainItem = mc.player.getMainHandStack();
                if (!ItemUtils.isFood(mainItem) && !isAllowedFood(mainItem)) {
                    for (int i = 0; i < 36; i++) {
                        ItemStack item = mc.player.getInventory().getStack(i);
                        if (ItemUtils.isFood(item) && isAllowedFood(item)) {
                            if (i < 9) {
                                InventoryUtils.swapItem(i);
                                foodEating = true;
                            } else {
                                int freeSlot = InventoryUtils.findFreeHotbarSlot();
                                if (freeSlot != -1) {
                                    InventoryUtils.swapItemOnInventory(freeSlot, i);
                                    InventoryUtils.swapItem(freeSlot);
                                    foodEating = true;
                                }
                            }
                            break;
                        }
                    }
                } else {
                    mc.options.useKey.setPressed(true);
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    foodEating = true;
                }
            } else {
                if (foodEating) {
                    mc.options.useKey.setPressed(KeyboardUtils.isKeyDown(mc.options.useKey.getDefaultKey().getCode()));
                    foodEating = false;
                }
            }
        }
    }

    private boolean isAllowedFood(ItemStack stack) {
        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        if (food == null) return false;
        if(!allowChorus.getValue() && food == FoodComponents.CHORUS_FRUIT)
            return false;

        if (!allowGapples.getValue() && isGolderApple(stack))
            return false;

        for (FoodComponent.StatusEffectEntry entry : food.effects()) {
            StatusEffect effect = entry.effect().getEffectType().value();

            if(effect == StatusEffects.HUNGER)
                return false;
            if(effect == StatusEffects.POISON)
                return false;
        }

        return true;
    }

    private boolean isGolderApple(ItemStack stack) {
        return stack.getItem() == Items.GOLDEN_APPLE || stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE;
    }
}
