package net.r_nik.extrashiny.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.item.VanadiumRepeaterItem;
import net.r_nik.extrashiny.item.OreTrackerUtil;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.item.MoondialItem;


public class ModItemProperties {

    public static void register() {
        ItemProperties.register(ModItems.RADAR.get(), new ResourceLocation("state"),
                (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) -> {

                    if (stack.hasTag()) {
                        int hostiles20 = stack.getTag().getInt("HostilesNearby20");
                        int hostiles10 = stack.getTag().getInt("HostilesNearby10");

                        long t = System.currentTimeMillis() / 200;

                        if (hostiles10 > 0) {
                            return 5 + (int)(t % 2);
                        }

                        if (hostiles20 > 0) {
                            return 1 + (int)(t % 4);
                        }
                    }

                    return 0;
                });

        ItemProperties.register(
                ModItems.MOONDIAL.get(),
                new ResourceLocation(ExtraShiny.MOD_ID, "state"),
                (stack, level, entity, seed) -> {
                    if (level == null && entity != null) {
                        if (entity.level() instanceof ClientLevel cl) level = cl;
                    }
                    if (level == null) return 8f;
                    return (float) MoondialItem.getMoondialState(level);
                }
        );

        ItemProperties.register(
                ModItems.RECALIBRATED_RADAR.get(),
                new ResourceLocation(ExtraShiny.MOD_ID, "state"),
                (stack, level, entity, seed) -> {
                    if (level == null || entity == null) return 0f;

                    if (!stack.hasTag()) return 0f;
                    int hostiles10 = stack.getTag().getInt("HostilesNearby10");
                    int hostiles20 = stack.getTag().getInt("HostilesNearby20");

                    long t = level.getGameTime();

                    if (hostiles10 > 0) {
                        return 10 + (t / 4) % 5;
                    }

                    if (hostiles20 > 0) {
                        return 1 + (t / 3) % 9; // 1..9
                    }

                    return 0f;
                }
        );



        ItemProperties.register(
                ModItems.ORE_TRACKER.get(),
                new ResourceLocation("state"),
                (stack, level, entity, seed) -> {

                    if (level == null || !stack.hasTag()) return 0f;

                    CompoundTag tag = stack.getTag();

                    if (!tag.contains("StoredItem")) return 0f;

                    int d = tag.getInt("NearestOreDist");

                    if (d < 0 || d > 80) return 0f;

                    long t = level.getGameTime();

                    if (d >= 65) return 1 + (t / 4) % 8;
                    if (d >= 49) return 9 + (t / 4) % 5;
                    if (d >= 33) return 14 + (t / 4) % 3;
                    if (d >= 17) return 17 + (t / 4) % 5;
                    return 22 + (t / 2) % 2;
                }
        );


        ItemProperties.register(
                ModItems.BULWARK.get(),
                new ResourceLocation("blocking"),
                (stack, level, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );

        ItemProperties.register(
                ModItems.VANADIUM_REPEATER.get(),
                new ResourceLocation("pulling"),
                (stack, level, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );

        ItemProperties.register(
                ModItems.VANADIUM_REPEATER.get(),
                new ResourceLocation("pull"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0.0F;
                    if (entity.getUseItem() != stack) return 0.0F;
                    return (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                }
        );

        ItemProperties.register(
                ModItems.VANADIUM_REPEATER.get(),
                new ResourceLocation("charged"),
                (stack, level, entity, seed) ->
                        VanadiumRepeaterItem.isCharged(stack) ? 1.0F : 0.0F
        );

        ItemProperties.register(
                ModItems.VANADIUM_REPEATER.get(),
                new ResourceLocation("firework"),
                (stack, level, entity, seed) ->
                        VanadiumRepeaterItem.containsChargedProjectile(stack, net.minecraft.world.item.Items.FIREWORK_ROCKET) ? 1.0F : 0.0F
        );
    }



    private static float lerp(int d, int max, int min, int fMin, int fMax) {
        float t = (float)(max - d) / (float)(max - min);
        return fMin + t * (fMax - fMin);
    }


}
