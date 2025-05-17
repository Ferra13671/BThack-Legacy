package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.core.Client.Client;
import com.ferra13671.BThack.api.IMixin.ModifyWindow;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.impl.HudComponents.OneTextComponents.ServerIPComponent;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.Icons;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ResourcePack;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Mixin(Window.class)
public class MixinWindow implements Mc, ModifyWindow {

    @Shadow @Final private long handle;

    @Inject(method = "setTitle", at = @At("HEAD"), cancellable = true)
    public void modifySetTitle(String title, CallbackInfo ci) {
        updateTitle();
        ci.cancel();
    }

    @Override
    public void updateTitle() {
        Client.clientInfo.updateName();
        String ip = ServerIPComponent.getIP();
        GLFW.glfwSetWindowTitle(handle, Client.clientInfo.getName() + (ip.isEmpty() ? "" : " | " + ip));
    }

    @Inject(method = "setIcon", at = @At("HEAD"), cancellable = true)
    public void modifySetIcon(ResourcePack resourcePack, Icons icons, CallbackInfo ci) {
        if (GLFW.glfwGetPlatform() == 393218) {
            return;
        }
        ci.cancel();

        setWindowIcon(BThack.class.getResourceAsStream("/assets/bthack/textures/bthack_icon.png"), BThack.class.getResourceAsStream("/assets/bthack/textures/bthack_icon.png"));
    }

    @Unique
    public void setWindowIcon(InputStream img16x16, InputStream img32x32) {
        try (MemoryStack memorystack = MemoryStack.stackPush()) {
            GLFWImage.Buffer buffer = GLFWImage.malloc(2, memorystack);
            List<InputStream> imgList = List.of(img16x16, img32x32);
            List<ByteBuffer> buffers = new ArrayList<>();

            for (int i = 0; i < imgList.size(); i++) {
                NativeImage nativeImage = NativeImage.read(imgList.get(i));
                ByteBuffer bytebuffer = MemoryUtil.memAlloc(nativeImage.getWidth() * nativeImage.getHeight() * 4);

                bytebuffer.asIntBuffer().put(nativeImage.copyPixelsAbgr());
                buffer.position(i);
                buffer.width(nativeImage.getWidth());
                buffer.height(nativeImage.getHeight());
                buffer.pixels(bytebuffer);

                buffers.add(bytebuffer);
            }

            try {
                if (GLFW.glfwGetPlatform() != GLFW.GLFW_PLATFORM_WAYLAND) {
                    GLFW.glfwSetWindowIcon(mc.getWindow().getHandle(), buffer);
                }
            } catch (Exception ignored) {}
            buffers.forEach(MemoryUtil::memFree);
        } catch (IOException ignored) {}
    }
}
