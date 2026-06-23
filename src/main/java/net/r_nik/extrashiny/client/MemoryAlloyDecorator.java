package net.r_nik.extrashiny.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.client.IItemDecorator;

public class MemoryAlloyDecorator implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

        if (!tag.contains("MemoryDurability")) return false;

        int memoryDura = tag.getInt("MemoryDurability");
        int maxMemoryDura = tag.getInt("MaxMemoryDurability");

        if (memoryDura <= 0) return false;
        int width = Math.round(13.0F - (float)(maxMemoryDura - memoryDura) * 13.0F / (float)maxMemoryDura);
        int color = 0x00e3ff;
        int y = stack.isDamaged() ? yOffset + 11 : yOffset + 13;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);

        guiGraphics.fill(xOffset + 2, y, xOffset + 2 + 13, y + 2, 0xFF000000);
        guiGraphics.fill(xOffset + 2, y, xOffset + 2 + width, y + 1, color | 0xFF000000);
        guiGraphics.pose().popPose();

        return false;
    }
}