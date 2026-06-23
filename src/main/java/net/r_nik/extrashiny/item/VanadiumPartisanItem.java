package net.r_nik.extrashiny.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.entity.VanadiumPartisanEntity;

public class VanadiumPartisanItem extends TridentItem {

    public VanadiumPartisanItem(Properties properties) {
        super(properties.rarity(Rarity.COMMON).attributes(
                ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "partisan_dmg"),
                                11.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "partisan_spd"),
                                -2.9D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()
        ));
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(ModItems.VANADIUM_INGOT.get()) || super.isValidRepairItem(toRepair, repair);
    }


    @Override
    public int getEnchantmentValue() {
        return 2;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.isEnchanted();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) return InteractionResultHolder.fail(stack);

        var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> riptide = registry.getHolderOrThrow(Enchantments.RIPTIDE);

        if (stack.getEnchantmentLevel(riptide) > 0 && !player.isInWaterOrRain()) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        int used = this.getUseDuration(stack, entity) - timeLeft;
        if (used < 10) return;

        var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        int riptide = stack.getEnchantmentLevel(registry.getHolderOrThrow(Enchantments.RIPTIDE));

        if (riptide > 0 && !player.isInWaterOrRain()) return;

        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

        if (riptide == 0) {
            if (!level.isClientSide) {
                VanadiumPartisanEntity partisan = new VanadiumPartisanEntity(level, player, stack.copy());
                partisan.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);

                if (player.getAbilities().instabuild) partisan.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                level.addFreshEntity(partisan);
                if (!player.getAbilities().instabuild) player.getInventory().removeItem(stack);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);        } else {
            float yaw = player.getYRot();
            float pitch = player.getXRot();
            float vx = -Mth.sin(yaw * (float)Math.PI / 180F) * Mth.cos(pitch * (float)Math.PI / 180F);
            float vy = -Mth.sin(pitch * (float)Math.PI / 180F);
            float vz =  Mth.cos(yaw * (float)Math.PI / 180F) * Mth.cos(pitch * (float)Math.PI / 180F);

            float speed = 3.0F * ((1.0F + riptide) / 4.0F);
            player.push(vx * speed, vy * speed, vz * speed);

            player.startAutoSpinAttack(20, 11.0F, stack);

            if (player.onGround()) player.move(MoverType.SELF, new Vec3(0.0, 1.2, 0.0));

            Holder<SoundEvent> riptideSound = switch (riptide) {
                case 3 -> SoundEvents.TRIDENT_RIPTIDE_3;
                case 2 -> SoundEvents.TRIDENT_RIPTIDE_2;
                default -> SoundEvents.TRIDENT_RIPTIDE_1;
            };

            level.playSound(null, player.getX(), player.getY(), player.getZ(), riptideSound, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }
}