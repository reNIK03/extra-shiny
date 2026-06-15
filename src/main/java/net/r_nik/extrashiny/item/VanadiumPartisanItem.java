package net.r_nik.extrashiny.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.r_nik.extrashiny.client.ModBlockEntityWithoutLevelRenderer;
import net.r_nik.extrashiny.entity.VanadiumPartisanEntity;

import java.util.function.Consumer;

public class VanadiumPartisanItem extends TridentItem {

    public VanadiumPartisanItem(Properties properties) {
        super(properties.rarity(Rarity.COMMON));
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(ModItems.VANADIUM_INGOT.get()) || super.isValidRepairItem(toRepair, repair);
    }
    
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ModBlockEntityWithoutLevelRenderer.INSTANCE;
            }
        });
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder =
                    ImmutableMultimap.builder();

            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                            "Weapon modifier", 11.0D, AttributeModifier.Operation.ADDITION));


            builder.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(BASE_ATTACK_SPEED_UUID,
                            "Weapon modifier", -2.9D, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }

        return super.getDefaultAttributeModifiers(slot);
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

        int damage = stack.getDamageValue();
        int max = stack.getMaxDamage();

        if (max - damage <= 1) {
            return InteractionResultHolder.fail(stack);
        }

        if (EnchantmentHelper.getRiptide(stack) > 0 && !player.isInWaterOrRain()) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        int used = this.getUseDuration(stack) - timeLeft;
        if (used < 10) return;

        int riptide = EnchantmentHelper.getRiptide(stack);

        if (riptide > 0 && !player.isInWaterOrRain()) return;

        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(entity.getUsedItemHand()));

        if (riptide == 0) {
            if (!level.isClientSide) {
                VanadiumPartisanEntity partisan =
                        new VanadiumPartisanEntity(level, player, stack.copy());

                partisan.shootFromRotation(
                        player,
                        player.getXRot(),
                        player.getYRot(),
                        0.0F,
                        2.5F,
                        1.0F
                );

                if (player.getAbilities().instabuild) {
                    partisan.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                level.addFreshEntity(partisan);

                if (!player.getAbilities().instabuild)
                    player.getInventory().removeItem(stack);
            }

            level.playSound(null, player, SoundEvents.TRIDENT_THROW,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        else {
            float yaw = player.getYRot();
            float pitch = player.getXRot();

            float vx = -Mth.sin(yaw * (float)Math.PI / 180F) * Mth.cos(pitch * (float)Math.PI / 180F);
            float vy = -Mth.sin(pitch * (float)Math.PI / 180F);
            float vz =  Mth.cos(yaw * (float)Math.PI / 180F) * Mth.cos(pitch * (float)Math.PI / 180F);

            float length = Mth.sqrt(vx * vx + vy * vy + vz * vz);
            float speed = 3.0F * ((1.0F + riptide) / 4.0F);

            vx = vx * speed / length;
            vy = vy * speed / length;
            vz = vz * speed / length;

            player.push(vx, vy, vz);
            player.startAutoSpinAttack(20);

            if (player.onGround()) {
                player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
            }

            SoundEvent riptideSound =
                    riptide >= 3 ? SoundEvents.TRIDENT_RIPTIDE_3 :
                            riptide == 2 ? SoundEvents.TRIDENT_RIPTIDE_2 :
                                    SoundEvents.TRIDENT_RIPTIDE_1;

            level.playSound(null, player, riptideSound,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }
}