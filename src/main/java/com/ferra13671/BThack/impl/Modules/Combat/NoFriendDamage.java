package com.ferra13671.BThack.impl.Modules.Combat;

import com.ferra13671.BThack.events.Entity.AttackEntityEvent;
import com.ferra13671.BThack.managers.Managers;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Module.ModuleInfo;
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
