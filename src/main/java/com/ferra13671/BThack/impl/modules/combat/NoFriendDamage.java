package com.ferra13671.BThack.impl.modules.combat;

import com.ferra13671.BThack.events.entity.AttackEntityEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.module.Module;
import com.ferra13671.BThack.api.module.ModuleInfo;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

@ModuleInfo(name = "NoFriendDamage", description = "lang.module.NoFriendDamage", category = "COMBAT")
public class NoFriendDamage extends Module {

    @EventSubscriber
    @SuppressWarnings({"unused", "DataFlowIssue"})
    public void onAttack(AttackEntityEvent e) {
        if (nullCheck()) return;

        if (Managers.FRIENDS_MANAGER.contains(e.getEntity().getDisplayName().getString())) {
            e.setCancelled(true);
        }
    }
}
