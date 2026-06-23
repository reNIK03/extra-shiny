package net.r_nik.extrashiny.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.r_nik.extrashiny.network.ModMessages;
import net.r_nik.extrashiny.network.RefiningButtonPacket;


public class RefiningTableScreen extends AbstractContainerScreen<RefiningTableMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("extrashiny", "textures/gui/refining_table_gui.png");

    private static final int TEX_WIDTH = 256;
    private static final int TEX_HEIGHT = 256;

    private static final int BUTTON_WIDTH = 40;
    private static final int BUTTON_HEIGHT = 18;

    private static final int REFINE_X = 24;
    private static final int REFINE_Y = 24;

    private static final int REFINE_U_NORMAL = 0;
    private static final int REFINE_U_HOVER = 40;
    private static final int BUTTON_V = 166;

    private static final int OVERCAP_X = 24;
    private static final int OVERCAP_Y = 42;

    private static final int OVERCAP_U_NORMAL = 80;
    private static final int OVERCAP_U_HOVER = 120;

    public RefiningTableScreen(RefiningTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 8;
        this.titleLabelY = 6;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        guiGraphics.blit(
                TEXTURE,
                leftPos,
                topPos,
                0,
                0,
                imageWidth,
                imageHeight,
                TEX_WIDTH,
                TEX_HEIGHT
        );

        renderRefineButton(guiGraphics, mouseX, mouseY);
        renderOvercapButton(guiGraphics, mouseX, mouseY);
    }

    private void renderRefineButton(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        var blockEntity = menu.getBlockEntity();

        if (blockEntity.canOvercapRefine()) return;
        if (menu.canOvercapRefine()) return;
        if (!blockEntity.canRefine()) return;

        int x = leftPos + REFINE_X;
        int y = topPos + REFINE_Y;

        boolean hovering =
                mouseX >= x && mouseX < x + BUTTON_WIDTH &&
                        mouseY >= y && mouseY < y + BUTTON_HEIGHT;

        int u = hovering ? REFINE_U_HOVER : REFINE_U_NORMAL;

        guiGraphics.blit(
                TEXTURE,
                x,
                y,
                u,
                BUTTON_V,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                TEX_WIDTH,
                TEX_HEIGHT
        );
    }

    private void renderOvercapButton(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!menu.getBlockEntity().canOvercapRefine()) return;

        int x = leftPos + OVERCAP_X;
        int y = topPos + OVERCAP_Y;

        boolean hovering =
                mouseX >= x && mouseX < x + BUTTON_WIDTH &&
                        mouseY >= y && mouseY < y + BUTTON_HEIGHT;

        int u = hovering ? OVERCAP_U_HOVER : OVERCAP_U_NORMAL;

        guiGraphics.blit(
                TEXTURE,
                x,
                y,
                u,
                BUTTON_V,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                TEX_WIDTH,
                TEX_HEIGHT
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        if (isMouseOverRefine(mouseX, mouseY) && menu.canNormalRefine()) {
            ModMessages.sendToServer(new RefiningButtonPacket(false));
            return true;
        }

        if (isMouseOverOvercap(mouseX, mouseY) && menu.canOvercapRefine()) {
            ModMessages.sendToServer(new RefiningButtonPacket(true));
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isMouseOverRefine(double mouseX, double mouseY) {
        int x = leftPos + 24;
        int y = topPos + 24;
        return mouseX >= x && mouseX < x + 40
                && mouseY >= y && mouseY < y + 18;
    }

    private boolean isMouseOverOvercap(double mouseX, double mouseY) {
        int x = leftPos + 24;
        int y = topPos + 42;
        return mouseX >= x && mouseX < x + 40
                && mouseY >= y && mouseY < y + 18;
    }
}