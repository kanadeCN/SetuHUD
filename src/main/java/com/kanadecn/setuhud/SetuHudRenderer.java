package com.kanadecn.setuhud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SetuHudRenderer {
    private static final Identifier TARGET_IMAGE = new Identifier("setuhud", "textures/images/hud.png");

    public static void render(MatrixStack matrices) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.options.hudHidden || client.player == null) return;

        int x = 10;
        int y = 10;
        int width = 64;
        int height = 64;

        RenderSystem.setShaderTexture(0, TARGET_IMAGE);
        client.inGameHud.drawTexture(matrices, x, y, 0, 0, width, height);
    }
}