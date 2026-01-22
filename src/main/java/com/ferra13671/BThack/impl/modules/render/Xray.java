package com.ferra13671.BThack.impl.modules.render;

import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.BThack.core.client.Client;
import com.ferra13671.BThack.events.SetOpaqueCubeEvent;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.utils.ChatUtils;
import com.ferra13671.BThack.api.utils.datalist.BlockList;
import com.ferra13671.BThack.api.utils.datalist.DataLists;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.util.Formatting;

@ModuleInfo(name = "Xray", description = "lang.module.Xray", category = "RENDER")
public class Xray extends Module {

    public static boolean doXray = false;


    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }

        super.onEnable();

        doXray = true;
        mc.worldRenderer.reload();
        ChatUtils.sendMessage(Formatting.GRAY + "Use: " + Client.clientInfo.getChatPrefix() + DataLists.get("Xray", BlockList.class).editDataListCommand.getAliases()[0]);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (nullCheck()) return;

        doXray = false;

        mc.worldRenderer.reload();
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onSetOpaque(SetOpaqueCubeEvent e) {
        e.setCancelled(true);
    }

    /*
    @EventSubscriber
    @SuppressWarnings("unused")
    public void onIsNormalCube(IsNormalCubeEvent e) {
        if (Xray.doXray) {
            BlockState state = mc.world.getBlockState(e.pos);
            if (state != null) {
                if (Client.blockLists.get("Xray").blocks.contains(state.getBlock())) {
                    e.setCancelled(true);
                }
            }
        }
    }

     */
}
