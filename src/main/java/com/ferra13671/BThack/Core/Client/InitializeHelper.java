package com.ferra13671.BThack.Core.Client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Systems.FirstLaunchWelcomer;
import com.ferra13671.BThack.Core.Client.Systems.HotbarSystem;
import com.ferra13671.BThack.Core.Client.Systems.KeyHandler;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Initializable;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Plugin.*;
import com.ferra13671.BThack.api.Utils.Grim.GrimFreezeUtils;
import com.ferra13671.SimpleLanguageSystem.LanguageSystem;
import com.ferra13671.TextureUtils.Controller.DefaultGlController;
import com.ferra13671.TextureUtils.GLTextureSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import java.lang.reflect.Field;

public final class InitializeHelper implements Mc {
    private static boolean hasInitedLibraries = false;
    private static boolean hasInitedManagers = false;

    static void initLibraries() {
        if (hasInitedLibraries) return;

        LanguageSystem.setCurrentLanguageGetter(() -> ModuleList.clientSettings.language.getValue());
        GLTextureSystem.setGlController(new DefaultGlController() {
            @Override
            public void run(Runnable runnable) {
                RenderSystem.recordRenderCall(runnable::run);
            }

            @Override
            public int genTexId() {
                return TextureUtil.generateTextureId();
            }

            @Override
            public void bindTexture(int id) {
                GlStateManager._bindTexture(id);
            }

            @Override
            public void deleteTexture(int id) {
                GlStateManager._deleteTexture(id);
            }
        });

        hasInitedLibraries = true;
    }

    static void initManagers() {
        if (hasInitedManagers) return;
        Field[] fields = Managers.class.getFields();
        for (Field field : fields) {
            try {
                Initializable manager = (Initializable) field.get(null);
                manager.init();
            } catch (IllegalAccessException e) {
                BThack.error(e.getMessage());
            }
        }

        BThack.EVENT_BUS.register(new GrimFreezeUtils());
        hasInitedManagers = true;
    }

    static void initCustomCategories() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitCustomCategories);
    }

    static void initSystems() {
        BThack.EVENT_BUS.register(new KeyHandler());
        BThack.EVENT_BUS.register(new HotbarSystem());
        if (BThack.instance.versionInfo.isFirstLaunched()) BThack.EVENT_BUS.register(new FirstLaunchWelcomer());
    }
}
