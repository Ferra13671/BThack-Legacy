package com.ferra13671.BThack.impl.Modules.MISC;

import com.ferra13671.BThack.Core.Client.Systems.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.Client.Systems.FileSystem.JsonUtils;
import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Rotate.RotateUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.AnvilBlock;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoAnvilEnchant extends Module {
    public static final HashMap<Item, HashMap<RegistryKey<Enchantment>, Integer>> recipes = new HashMap<>();

    public static void save() throws IOException {
        ConfigUtils.saveInJson("AutoAnvilEnchant", "", jsonObject -> {
            JsonArray list = new JsonArray();
            recipes.forEach((item, enchants) -> {
                JsonObject itemObject = new JsonObject();
                JsonUtils.add(itemObject, "Item", ItemUtils.getItemName(item));
                JsonArray enchantList = new JsonArray();
                enchants.forEach((enchant, level) -> {
                    JsonObject enchantObject = new JsonObject();
                    JsonUtils.add(enchantObject, "Enchantment", enchant.getValue().getPath());
                    JsonUtils.add(enchantObject, "Level", level);
                    enchantList.add(enchantObject);
                });
                itemObject.add("Enchantments", enchantList);
                list.add(itemObject);
            });
            jsonObject.add("List", list);
        });
    }

    public static void load() throws IOException {
        ConfigUtils.loadFromJson("AutoAnvilEnchant", "", jsonObject -> {
            if (!JsonUtils._null(jsonObject, "List")) {
                JsonArray jsonArray = jsonObject.get("List").getAsJsonArray();
                jsonArray.forEach(jsonElement -> {
                    if (jsonElement.isJsonObject()) {
                        JsonObject itemObject = jsonElement.getAsJsonObject();
                        if (!JsonUtils._null(itemObject, "Item")) {
                            if (!JsonUtils._null(itemObject, "Enchantments")) {
                                Item item = ItemUtils.getItemFromName(itemObject.get("Item").getAsString());
                                HashMap<RegistryKey<Enchantment>, Integer> enchantments = new HashMap<>();
                                JsonArray enchantList = itemObject.get("Enchantments").getAsJsonArray();
                                enchantList.forEach(jsonElement1 -> {
                                    if (jsonElement1.isJsonObject()) {
                                        JsonObject enchantObject = jsonElement1.getAsJsonObject();
                                        if (!JsonUtils._null(enchantObject, "Enchantment") && !JsonUtils._null(enchantObject, "Level")) {
                                            enchantments.put(Lists.ENCHANTMENTS.get(enchantObject.get("Enchantment").getAsString()), enchantObject.get("Level").getAsInt());
                                        }
                                    }
                                });
                                if (!enchantments.isEmpty())
                                    recipes.put(item,enchantments);
                            }
                        }
                    }
                });
            }
        }, () -> {});
    }

    public final NumberSetting applyDelay = new NumberSetting("Apply Delay", this, 0, 0, 300, true);

    public AutoAnvilEnchant() {
        super("AutoAnvilEnchant",
                "lang.module.AutoAnvilEnchant",
                KeyboardUtils.RELEASE,
                MCategory.MISC,
                false
        );
    }
    private final Ticker delayTicker = new Ticker();
    private PostClickInfo postClickInfo = null;

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        BlockPos anvilBlockPos = null;

        if (postClickInfo != null) {
            pc.clickSlot(postClickInfo.syncId, 2, 0, SlotActionType.PICKUP);
            pc.tick();
            pc.clickSlot(postClickInfo.syncId, postClickInfo.slot, 0, SlotActionType.PICKUP);
            pc.tick();

            mc.player.closeHandledScreen();

            postClickInfo = null;
            return;
        }

        //Anvil search
        for (BlockPos blockPos : BlockUtils.getSphere(mc.player.getBlockPos(), 4, 4, false, true, 0)) {
            if (mc.world.getBlockState(blockPos).getBlock() instanceof AnvilBlock) {
                anvilBlockPos = blockPos;
                break;
            }
        }
        if (anvilBlockPos != null) {

            //anvilBlockPos = anvilBlockPos.add(1, 0, 0);
            for (Item item : recipes.keySet()) {
                HashMap<RegistryKey<Enchantment>, Integer> needEnchantments = new HashMap<>(recipes.get(item));
                //All slots with the desired item, as well as the right enchantments for it.
                //This list is needed so that if 1 item does not fit the conditions, you can switch to the next item.
                List<ItemInfo> infos = findItems(item, needEnchantments);

                if (infos.isEmpty()) continue;

                internalAction(infos.getFirst(), infos, anvilBlockPos);
            }
        }
    }

    public void internalAction(ItemInfo itemInfo, List<ItemInfo> itemInfos, BlockPos anvilBlockPos) {
        int slot = itemInfo.slot;
        ItemStack itemStack = mc.player.getInventory().getStack(slot);
        List<Integer> correctEnchantBooks = new ArrayList<>();
        List<String> availableEnchantments = new ArrayList<>();

        //Search for all matching enchanted books
        for (int i = 0; i < 36; i++) {
            ItemStack inventoryItem = mc.player.getInventory().getStack(i);
            enchantSearch: {
                if (inventoryItem.getItem() instanceof EnchantedBookItem) {
                    for (RegistryKey<Enchantment> enchantment : itemInfo.needEnchantments.keySet()) {
                        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> itemEnchantment : EnchantmentHelper.getEnchantments(inventoryItem).getEnchantmentEntries()) {
                            String ench = enchantment.getValue().toString();
                            if (itemEnchantment.getKey().getKey().get().getValue().toString().equals(
                                    ench) && itemEnchantment.getIntValue() == itemInfo.needEnchantments.get(enchantment)) {
                                correctEnchantBooks.add(i);
                                availableEnchantments.add(ench);
                                break enchantSearch;
                            }
                        }
                    }
                }
            }
        }

        //Removing from the list of enchantments that have already been applied to an item
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> itemEnchantment : EnchantmentHelper.getEnchantments(itemStack).getEnchantmentEntries())
            availableEnchantments.remove(itemEnchantment.getKey().getKey().get().getValue().toString());


        if (availableEnchantments.isEmpty()) {
            if (itemInfos.size() > 1) {
                itemInfos.removeFirst();
                internalAction(itemInfos.getFirst(), itemInfos, anvilBlockPos);
            }
            return;
        }

        if (!correctEnchantBooks.isEmpty()) {
            int level = mc.player.experienceLevel;

            int nextBookSlot = -1;
            //Finding a book that can be applied to an item
            for (Integer bookSlot : correctEnchantBooks) {
                if (getLevelToMerge(itemStack, mc.player.getInventory().getStack(bookSlot)) < level || mc.player.isCreative()) {
                    nextBookSlot = bookSlot;
                    break;
                }
            }
            if (nextBookSlot == -1) {
                if (itemInfos.size() > 1) {
                    itemInfos.removeFirst();
                    internalAction(itemInfos.getFirst(), itemInfos, anvilBlockPos);
                }
                return;
            }

            if (!(mc.currentScreen instanceof AnvilScreen screen)) {
                if (mc.currentScreen != null) pc.closeScreen();
                boolean releaseSneak = mc.player.isSneaking();
                if (releaseSneak)
                    Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
                pc.interactBlock(mc.player, Hand.MAIN_HAND, BuildManager.getHitResult(anvilBlockPos, false, RotateUtils.getInvertedFacingEntity(mc.player)));
                if (releaseSneak)
                    Managers.NETWORK_MANAGER.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
            } else if (delayTicker.passed(applyDelay.getValue())) {
                delayTicker.reset();
                AnvilScreenHandler screenHandler = screen.getScreenHandler();
                //Slot numbering fix
                int _slot = slot < 9 ? slot + 30 : slot - 6;
                int _nextBookSlot = nextBookSlot < 9 ? nextBookSlot + 30 : nextBookSlot - 6;

                pc.clickSlot(screenHandler.syncId, _slot, 0, SlotActionType.QUICK_MOVE);
                pc.tick();
                pc.clickSlot(screenHandler.syncId, _nextBookSlot, 0, SlotActionType.QUICK_MOVE);
                pc.tick();

                postClickInfo = new PostClickInfo(_slot, screenHandler.syncId);
            }
        }
    }

    public List<ItemInfo> findItems(Item item, HashMap<RegistryKey<Enchantment>, Integer> needEnchantments) {
        List<ItemInfo> items = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                HashMap<RegistryKey<Enchantment>, Integer> tempEnchantments = new HashMap<>(needEnchantments);
                //Removes enchantments from the list that have already been applied to an item//
                List<RegistryKey<Enchantment>> removableEnchantments = new ArrayList<>();
                for (RegistryKey<Enchantment> enchantment : needEnchantments.keySet()) {
                    for (RegistryEntry<Enchantment> itemEnchantment : stack.getEnchantments().getEnchantments()) {
                        if (itemEnchantment.equals(mc.world.getRegistryManager().get(enchantment.getRegistryRef()).getEntry(enchantment).get())) {
                            removableEnchantments.add(enchantment);
                            break;
                        }
                    }
                }
                removableEnchantments.forEach(tempEnchantments::remove);
                //----------------------------------------------------------------------------//
                //If the list of need enchantments is empty, skip the item
                if (!tempEnchantments.isEmpty())
                    items.add(new ItemInfo(i, tempEnchantments));
            }
        }
        return items;
    }

    public int getLevelToMerge(ItemStack itemStack1, ItemStack itemStack2) {
        int level;


        int i = 0;
        long l = 0L;
        int j = 0;
        if (!itemStack1.isEmpty() && EnchantmentHelper.canHaveEnchantments(itemStack1)) {
            ItemStack itemStack1Copy = itemStack1.copy();
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(itemStack1Copy));
            l += (long)itemStack1.getOrDefault(DataComponentTypes.REPAIR_COST, 0) + (long)itemStack2.getOrDefault(DataComponentTypes.REPAIR_COST, 0);

            int k;
            if (!itemStack2.isEmpty()) {
                boolean bl = itemStack2.contains(DataComponentTypes.STORED_ENCHANTMENTS);
                int m;
                int n;
                if (itemStack1Copy.isDamageable() && itemStack1Copy.getItem().canRepair(itemStack1, itemStack2)) {
                    k = Math.min(itemStack1Copy.getDamage(), itemStack1Copy.getMaxDamage() / 4);
                    if (k <= 0) {
                        level = 0;
                        return level;
                    }

                    for(m = 0; k > 0 && m < itemStack2.getCount(); ++m) {
                        n = itemStack1Copy.getDamage() - k;
                        itemStack1Copy.setDamage(n);
                        ++i;
                        k = Math.min(itemStack1Copy.getDamage(), itemStack1Copy.getMaxDamage() / 4);
                    }
                } else {
                    if (!bl && (!itemStack1Copy.isOf(itemStack2.getItem()) || !itemStack1Copy.isDamageable())) {
                        level = 0;
                        return level;
                    }

                    if (itemStack1Copy.isDamageable() && !bl) {
                        k = itemStack1.getMaxDamage() - itemStack1.getDamage();
                        m = itemStack2.getMaxDamage() - itemStack2.getDamage();
                        n = m + itemStack1Copy.getMaxDamage() * 12 / 100;
                        int o = k + n;
                        int p = itemStack1Copy.getMaxDamage() - o;
                        if (p < 0) {
                            p = 0;
                        }

                        if (p < itemStack1Copy.getDamage()) {
                            itemStack1Copy.setDamage(p);
                            i += 2;
                        }
                    }

                    ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack2);
                    boolean bl2 = false;
                    boolean bl3 = false;

                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> registryEntryEntry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                        RegistryEntry<Enchantment> registryEntry = registryEntryEntry.getKey();
                        int q = builder.getLevel(registryEntry);
                        int r = registryEntryEntry.getIntValue();
                        r = q == r ? r + 1 : Math.max(r, q);
                        Enchantment enchantment = registryEntry.value();
                        boolean bl4 = enchantment.isAcceptableItem(itemStack1);
                        if (mc.player.getAbilities().creativeMode || itemStack1.isOf(Items.ENCHANTED_BOOK)) {
                            bl4 = true;
                        }

                        for (RegistryEntry<Enchantment> enchantmentRegistryEntry : builder.getEnchantments()) {
                            if (!enchantmentRegistryEntry.equals(registryEntry) && !Enchantment.canBeCombined(registryEntry, enchantmentRegistryEntry)) {
                                bl4 = false;
                                ++i;
                            }
                        }

                        if (!bl4) {
                            bl3 = true;
                        } else {
                            bl2 = true;
                            if (r > enchantment.getMaxLevel()) {
                                r = enchantment.getMaxLevel();
                            }

                            builder.set(registryEntry, r);
                            int s = enchantment.getAnvilCost();
                            if (bl) {
                                s = Math.max(1, s / 2);
                            }

                            i += s * r;
                            if (itemStack1.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }

                    if (bl3 && !bl2) {
                        level = 0;
                        return level;
                    }
                }
            }

            if (itemStack1.contains(DataComponentTypes.CUSTOM_NAME)) {
                j = 1;
                i += j;
                itemStack1Copy.remove(DataComponentTypes.CUSTOM_NAME);
            }

            level = (int)MathHelper.clamp(l + (long)i, 0L, 2147483647L);
            if (i <= 0) {
                itemStack1Copy = ItemStack.EMPTY;
            }

            if (j == i && j > 0 && level >= 40) {
                level = 39;
            }

            if (level >= 40 && !mc.player.getAbilities().creativeMode) {
                itemStack1Copy = ItemStack.EMPTY;
            }

            if (!itemStack1Copy.isEmpty()) {
                k = itemStack1Copy.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
                if (k < itemStack2.getOrDefault(DataComponentTypes.REPAIR_COST, 0)) {
                    k = itemStack2.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
                }

                if (j != i || j == 0) {
                    k = getNextCost(k);
                }

                itemStack1Copy.set(DataComponentTypes.REPAIR_COST, k);
                EnchantmentHelper.set(itemStack1Copy, builder.build());
            }

        } else {
            level = 0;
            return level;
        }
        return level;
    }

    public static int getNextCost(int cost) {
        return (int)Math.min((long)cost * 2L + 1L, 2147483647L);
    }

    public record ItemInfo(int slot, HashMap<RegistryKey<Enchantment>, Integer> needEnchantments) {}

    public record PostClickInfo(int slot, int syncId) {}
}
