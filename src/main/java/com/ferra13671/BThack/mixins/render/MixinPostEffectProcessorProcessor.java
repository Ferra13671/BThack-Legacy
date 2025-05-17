package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.api.IMixin.ModifyPostEffectProcessor;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PostEffectProcessor.class)
public class MixinPostEffectProcessorProcessor implements ModifyPostEffectProcessor {
    @Override
    public void _addTargetHook(String name, Framebuffer buffer) {

    }
    /*
    @Unique
    private final List<String> fakedBufferNames = new ArrayList<>();
    @Shadow
    @Final
    private Map<String, Framebuffer> targetsByName;
    @Shadow @Final private List<PostEffectPass> passes;


    @Override
    public void _addTargetHook(String name, Framebuffer buffer) {
        Framebuffer previousFramebuffer = this.targetsByName.get(name);
        if (previousFramebuffer == buffer) {
            return;
        }
        if (previousFramebuffer != null) {
            for (PostEffectPass pass : this.passes) {
                if (pass.input == previousFramebuffer) ((IPostEffectPass) pass).setInput(buffer);
                if (pass.output == previousFramebuffer) ((IPostEffectPass) pass).setOutput(buffer);
            }
            this.targetsByName.remove(name);
            this.fakedBufferNames.remove(name);
        }

        this.targetsByName.put(name, buffer);
        this.fakedBufferNames.add(name);
    }

    @Inject(method = "close", at = @At("HEAD"))
    void modifyClose(CallbackInfo ci) {
        for (String fakedBufferName : fakedBufferNames)
            targetsByName.remove(fakedBufferName);
    }

     */
}
