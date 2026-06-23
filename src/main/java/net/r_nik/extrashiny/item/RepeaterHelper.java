package net.r_nik.extrashiny.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class RepeaterHelper {

    public static List<ItemStack> getProjectiles(ItemStack crossbow) {
        ChargedProjectiles charged = crossbow.get(DataComponents.CHARGED_PROJECTILES);
        if (charged == null || charged.isEmpty()) return List.of();
        return charged.getItems();
    }

    public static boolean stackHasFirework(ItemStack crossbow) {
        ChargedProjectiles charged = crossbow.get(DataComponents.CHARGED_PROJECTILES);
        return charged != null && charged.contains(Items.FIREWORK_ROCKET);
    }

    public static AbstractArrow createArrow(Level level, LivingEntity shooter, ItemStack crossbow, ItemStack ammo) {
        if (ammo.isEmpty() || ammo.is(Items.FIREWORK_ROCKET)) return null;

        ArrowItem arrowItem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);

        AbstractArrow arrow = arrowItem.createArrow(level, ammo, shooter, crossbow);

        if (shooter instanceof Player) {
            arrow.setCritArrow(true);
        }

        arrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);

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

        var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> multishotEnch = registry.getHolderOrThrow(Enchantments.MULTISHOT);

        boolean hasMultishot = crossbow.getEnchantmentLevel(multishotEnch) > 0;

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

                if (shooter instanceof Player player) {
                    if (!player.getAbilities().instabuild) {
                        boolean consumed = consumeAmmoFromPlayer(player, ammoPrototype);
                        if (!consumed) {
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

                    if (!level.isClientSide) {
                        level.addFreshEntity(rocket);
                    }
                    continue;
                }

                AbstractArrow arrow = createArrow(level, shooter, crossbow, ammoPrototype);
                if (arrow == null) continue;

                arrow.setBaseDamage(arrow.getBaseDamage() * 0.70);
                arrow.invulnerableTime = 0;


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
                    arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY; // standard vanilla multishot behavior
                }

                if (!level.isClientSide) {
                    level.addFreshEntity(arrow);
                }
            }
        }
    }

    private static boolean consumeAmmoFromPlayer(Player player, ItemStack prototype) {
        if (player.getAbilities().instabuild) return true;

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack slot = player.getInventory().items.get(i);
            if (slot.isEmpty()) continue;

            if (ItemStack.isSameItemSameComponents(slot, prototype)) {
                slot.shrink(1);
                if (slot.isEmpty()) {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                }
                return true;
            }
        }

        return false;
    }
}