package dev.kanadecn.setuhud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SetuHUD implements ClientModInitializer {
    private static final Identifier CUSTOM_IMAGE = new Identifier("setuhud", "textures/gui/image.png");
    private int imageWidth = 0;  // 动态宽度
    private int imageHeight = 0; // 动态高度
    private boolean textureLoaded = false;

    @Override
    public void onInitializeClient() {
        // 预加载纹理资源
        loadTextureDimensions();
        HudRenderCallback.EVENT.register(this::renderCustomImage);
    }

    private void loadTextureDimensions() {
        MinecraftClient.getInstance().execute(() -> {
            TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
            try {
                // 获取纹理元数据
                var resource = MinecraftClient.getInstance().getResourceManager()
                    .getResource(CUSTOM_IMAGE)
                    .orElseThrow();
                
                // 使用NativeImage读取图片尺寸
                try (var inputStream = resource.getInputStream()) {
                    var image = net.minecraft.client.texture.NativeImage.read(inputStream);
                    imageWidth = image.getWidth();
                    imageHeight = image.getHeight();
                    textureLoaded = true;
                }
            } catch (Exception e) {
                System.err.println("Failed to load image dimensions: " + e.getMessage());
                // 设置默认尺寸防止崩溃
                imageWidth = 64;
                imageHeight = 64;
            }
        });
    }

    private void renderCustomImage(MatrixStack matrices, float tickDelta) {
        if (!textureLoaded) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) return;

        Window window = client.getWindow();
        int screenWidth = window.getScaledWidth();
        int screenHeight = window.getScaledHeight();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CUSTOM_IMAGE);

        // 动态计算位置（示例：右下角带5像素边距）
        int x = screenWidth - imageWidth - 5;
        int y = screenHeight - imageHeight - 5;

        DrawableHelper.drawTexture(
            matrices,
            x, y,
            0, 0,
            imageWidth, imageHeight,
            imageWidth, imageHeight
        );
    }
}
