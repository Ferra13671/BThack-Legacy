package com.ferra13671.BThack.api.Managers.managers.Cape;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.mixins.accessor.INativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.IntBuffer;

public class Cape implements Closeable, Mc {
    private Identifier texture = Identifier.of("bthack", "capes/cape-" + MathUtils.randomString(10, false, false, false));

    @Override
    public void close() {
        mc.getTextureManager().destroyTexture(texture);
    }

    public Identifier getTexture() {
        return texture;
    }

    public static Cape fromInputStream(InputStream inputStream) throws IOException {
        Cape cape = new Cape();
        BufferedImage image = ImageIO.read(inputStream);
        registerInMinecraft(cape.texture, image);
        return cape;
    }

    public static Cape fromIdentifier(Identifier identifier) {
        Cape cape = new Cape() {
            @Override
            public void close() {
            }
        };
        cape.texture = identifier;
        return cape;
    }

    public static Cape fromURL(URL url) throws IOException {
        Cape cape = new Cape();
        BufferedImage image = ImageIO.read(url);
        registerInMinecraft(cape.texture, image);
        return cape;
    }

    private static void registerInMinecraft(Identifier i, BufferedImage bi) {
        try {
            int ow = bi.getWidth();
            int oh = bi.getHeight();
            NativeImage image = new NativeImage(NativeImage.Format.RGBA, ow, oh, false);
            @SuppressWarnings("DataFlowIssue") long ptr = ((INativeImage) (Object) image).getPointer();
            IntBuffer backingBuffer = MemoryUtil.memIntBuffer(ptr, image.getWidth() * image.getHeight());
            Object _d;
            WritableRaster _ra = bi.getRaster();
            ColorModel _cm = bi.getColorModel();
            int nbands = _ra.getNumBands();
            int dataType = _ra.getDataBuffer().getDataType();
            _d = switch (dataType) {
                case DataBuffer.TYPE_BYTE -> new byte[nbands];
                case DataBuffer.TYPE_USHORT -> new short[nbands];
                case DataBuffer.TYPE_INT -> new int[nbands];
                case DataBuffer.TYPE_FLOAT -> new float[nbands];
                case DataBuffer.TYPE_DOUBLE -> new double[nbands];
                default -> throw new IllegalArgumentException("Unknown data buffer type: " +
                        dataType);
            };

            for (int y = 0; y < oh; y++) {
                for (int x = 0; x < ow; x++) {
                    _ra.getDataElements(x, y, _d);
                    int a = _cm.getAlpha(_d);
                    int r = _cm.getRed(_d);
                    int g = _cm.getGreen(_d);
                    int b = _cm.getBlue(_d);
                    int abgr = a << 24 | b << 16 | g << 8 | r;
                    backingBuffer.put(abgr);
                }
            }
            NativeImageBackedTexture tex = new NativeImageBackedTexture(image);
            tex.upload();
            if (RenderSystem.isOnRenderThread()) {
                mc.getTextureManager().registerTexture(i, tex);
            } else {
                RenderSystem.recordRenderCall(() -> mc.getTextureManager().registerTexture(i, tex));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
