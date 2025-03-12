package dev.kanadecn.setuhud;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class SetuHUD implements ClientModInitializer {
    private static final Identifier CUSTOM_IMAGE = new Identifier("setuhud", "textures/gui/image.png");
    
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(this::renderCustomImage);
    }

    private void renderCustomImage(MatrixStack matrices, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) return; // 仅在游戏界面显示
        
        // 获取窗口尺寸
        Window window = client.getWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();
        
        // 设置渲染参数
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CUSTOM_IMAGE);
        
        // 在右下角显示（示例位置）
        int imageSize = 64;
        int x = width - imageSize - 5;
        int y = height - imageSize - 5;
        
        // 绘制纹理
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, imageSize, imageSize, imageSize, imageSize);
    }
}
