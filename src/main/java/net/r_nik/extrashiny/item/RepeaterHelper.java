package net.r_nik.extrashiny.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class RepeaterHelper {

    public static List<ItemStack> getProjectiles(ItemStack crossbow) {
        List<ItemStack> list = new ArrayList<>();
        CompoundTag tag = crossbow.getTag();
        if (tag == null) return list;
        ListTag projList = tag.getList("ChargedProjectiles", 10);
        for (int i = 0; i < projList.size(); i++) {
            list.add(ItemStack.of(projList.getCompound(i)));
        }
        return list;
    }


    public static boolean stackHasFirework(ItemStack crossbow) {
        for (ItemStack s : getProjectiles(crossbow)) {
            if (s.is(Items.FIREWORK_ROCKET)) return true;
        }
        return false;
    }


    public static AbstractArrow createArrow(Level level, LivingEntity shooter, ItemStack crossbow, ItemStack ammo) {
        if (ammo.isEmpty()) return null;
        if (ammo.is(Items.FIREWORK_ROCKET)) return null;

        ArrowItem arrowItem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
        AbstractArrow arrow = arrowItem.createArrow(level, ammo, shooter);

        arrow.setShotFromCrossbow(true);

        int pierce = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, crossbow);
        if (pierce > 0) {
            try { arrow.setPierceLevel((byte) pierce); } catch (Throwable ignored) {}
        }

        if (shooter instanceof net.minecraft.world.entity.player.Player) {
            arrow.setCritArrow(true);
        }

        try { arrow.setSoundEvent(net.minecraft.sounds.SoundEvents.CROSSBOW_HIT); } catch (Throwable ignored) {}

        return arrow;
    }

    public static void fireSingleVolley(
            Level level,
            LivingEntity shooter,
            ItemStack crossbow,
            float velocity,
            float inaccuracy,
            float baseAngleOffset
    ) {
        List<ItemStack> projectiles = getProjectiles(crossbow);
        if (projectiles.isEmpty()) return;

        boolean hasMultishot =
                EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, crossbow) > 0;

        if (hasMultishot && projectiles.size() > 1) {
            projectiles = List.of(projectiles.get(0));
        }

        float[] angles = hasMultishot
                ? new float[]{-10f, 0f, +10f}
                : new float[]{0f};

        boolean firstArrow = true;

        for (float extraAngle : angles) {
            for (ItemStack ammoPrototype : projectiles) {

                float totalAngle = baseAngleOffset + extraAngle;

                if (shooter instanceof net.minecraft.world.entity.player.Player player) {
                    if (!player.getAbilities().instabuild) { // not creative
                        boolean consumed = consumeAmmoFromPlayer(player, ammoPrototype);
                        if (!consumed) {
                            // No ammo left → skip this shot
                            continue;
                        }
                    }
                }

                if (ammoPrototype.is(Items.FIREWORK_ROCKET)) {
                    FireworkRocketEntity rocket = new FireworkRocketEntity(
                            level,
                            ammoPrototype,
                            shooter,
                            shooter.getX(),
                            shooter.getEyeY() - 0.15,
                            shooter.getZ(),
                            true
                    );

                    rocket.shootFromRotation(
                            shooter,
                            shooter.getXRot(),
                            shooter.getYRot() + totalAngle,
                            0.0F,
                            velocity,
                            inaccuracy
                    );

                    if (!level.isClientSide)
                        level.addFreshEntity(rocket);

                    continue;
                }

                AbstractArrow arrow = createArrow(level, shooter, crossbow, ammoPrototype);
                if (arrow == null) continue;

                arrow.setBaseDamage(arrow.getBaseDamage() * 0.70);
                arrow.invulnerableTime = 0;
                arrow.getPersistentData().putBoolean("VanadiumRepeater_NoIFrames", true);
                arrow.getPersistentData().putBoolean("VanadiumRepeater_OverridePiercing", true);

                arrow.shootFromRotation(
                        shooter,
                        shooter.getXRot(),
                        shooter.getYRot() + totalAngle,
                        0.0F,
                        velocity,
                        inaccuracy
                );

                if (firstArrow) {
                    arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                    firstArrow = false;
                } else {
                    arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                }

                if (!level.isClientSide)
                    level.addFreshEntity(arrow);
            }
        }
    }

    private static boolean consumeAmmoFromPlayer(
            net.minecraft.world.entity.player.Player player,
            ItemStack prototype
    ) {
        if (player.getAbilities().instabuild) return true;

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack slot = player.getInventory().items.get(i);
            if (slot.isEmpty()) continue;

            if (!slot.is(prototype.getItem())) continue;

            if (prototype.hasTag()) {
                if (!slot.hasTag()) continue;
                if (!slot.getTag().equals(prototype.getTag())) continue;
            }

            slot.shrink(1);
            if (slot.isEmpty()) {
                player.getInventory().items.set(i, ItemStack.EMPTY);
            }

            return true;
        }

        return false;
    }

}


