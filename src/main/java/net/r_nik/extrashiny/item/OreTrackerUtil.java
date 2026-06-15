package net.r_nik.extrashiny.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class OreTrackerUtil {

    public static final int MAX_RADIUS = 80;

    public static int findNearestOreDistance(Level level, BlockPos origin, ItemStack tracker) {
        if (!tracker.hasTag()) return -1;

        ItemStack stored = getStoredItem(tracker);
        if (stored.isEmpty()) return -1;

        TagKey<Block> oreTag = getOreTagForItem(stored);
        if (oreTag == null) return -1;

        final int MAX_RADIUS = 80;

        int closest = Integer.MAX_VALUE;

        for (int dx = -MAX_RADIUS; dx <= MAX_RADIUS; dx++) {
            for (int dy = -MAX_RADIUS; dy <= MAX_RADIUS; dy++) {
                for (int dz = -MAX_RADIUS; dz <= MAX_RADIUS; dz++) {

                    BlockPos pos = origin.offset(dx, dy, dz);

                    int dist = Math.max(
                            Math.max(Math.abs(dx), Math.abs(dy)),
                            Math.abs(dz)
                    );

                    if (dist >= closest) continue;
                    if (dist > MAX_RADIUS) continue;

                    if (level.getBlockState(pos).is(oreTag)) {
                        closest = dist;
                    }
                }
            }
        }

        return closest == Integer.MAX_VALUE ? -1 : closest;
    }


    public static ItemStack getStoredItem(ItemStack tracker) {
        if (!tracker.hasTag()) return ItemStack.EMPTY;

        var tag = tracker.getTag();
        if (!tag.contains("StoredItem")) return ItemStack.EMPTY;

        return ItemStack.of(tag.getCompound("StoredItem"));
    }

    public static boolean hasStoredItem(ItemStack tracker) {
        return tracker.hasTag()
                && tracker.getTag().contains("StoredItem")
                && !tracker.getTag().getCompound("StoredItem").isEmpty();
    }


    public static TagKey<Block> getOreTagForItem(ItemStack sample) {

        for (TagKey<net.minecraft.world.item.Item> tag : sample.getTags().toList()) {
            ResourceLocation id = tag.location();

            if (id.getNamespace().equals("forge")) {

                String path = id.getPath();

                if (path.startsWith("ingots/") ||
                        path.startsWith("raw_materials/") ||
                        path.startsWith("gems/")) {

                    String material = path.substring(path.indexOf('/') + 1);

                    return TagKey.create(
                            Registries.BLOCK,
                            new ResourceLocation("forge", "ores/" + material)
                    );
                }
            }
        }
        return null;
    }
}
