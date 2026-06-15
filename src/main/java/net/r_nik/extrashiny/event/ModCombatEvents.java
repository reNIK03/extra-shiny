package net.r_nik.extrashiny.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.attribute.ModAttributes;
import net.r_nik.extrashiny.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.r_nik.extrashiny.item.ModItems;

import java.util.Map;

@Mod.EventBusSubscriber
public class ModCombatEvents {

    private static final String TAG_REBOUND = "extrashiny_damage_rebound";
    private static final float REBOUND_CAP = 30.0F;
    private static final String TAG_DAMASK_PRE_ABSORB_DMG = "extrashiny_damask_pre_absorb_dmg";
    private static final String TAG_DAMASK_PRE_ABSORB_TICK = "extrashiny_damask_pre_absorb_tick";
    private static final String TAG_OSMIUM_PIERCED_DMG = "extrashiny_osmium_pierced_dmg";
    private static final String TAG_OSMIUM_PIERCED_TICK = "extrashiny_osmium_pierced_tick";

    private static float computeDamageBeforeAbsorption(LivingEntity target, DamageSource src, float amount) {
        float dmg = amount;
        if (!src.is(DamageTypeTags.BYPASSES_ARMOR)) {
            float armor = target.getArmorValue();
            float toughness = (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
            dmg = CombatRules.getDamageAfterAbsorb(dmg, armor, toughness);
        }
        if (!src.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            int prot = EnchantmentHelper.getDamageProtection(target.getArmorSlots(), src);
            dmg = CombatRules.getDamageAfterMagicAbsorb(dmg, prot);
        }
        if (!src.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            MobEffectInstance res = target.getEffect(MobEffects.DAMAGE_RESISTANCE);
            if (res != null) {
                int level = res.getAmplifier() + 1;
                float mult = 1.0F - 0.20F * level;
                if (mult < 0.0F) mult = 0.0F;
                dmg *= mult;
            }
        }
        return Math.max(0.0F, dmg);
    }

    private static float computePiercedDamage(LivingEntity target, DamageSource src, float amount, float piercingPct) {
        float dmg = amount;

        if (!src.is(DamageTypeTags.BYPASSES_ARMOR)) {
            float armor = target.getArmorValue() * (1.0F - piercingPct);
            float toughness = (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS) * (1.0F - piercingPct);
            dmg = CombatRules.getDamageAfterAbsorb(dmg, armor, toughness);
        }

        if (!src.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            int prot = EnchantmentHelper.getDamageProtection(target.getArmorSlots(), src);
            float effectiveProt = prot * (1.0F - piercingPct);
            float magicReduction = Math.min(effectiveProt, 20.0F) / 25.0F; // Vanilla math translation
            dmg = dmg * (1.0F - magicReduction);
        }

        if (!src.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            MobEffectInstance res = target.getEffect(MobEffects.DAMAGE_RESISTANCE);
            if (res != null) {
                int level = res.getAmplifier() + 1;
                float mult = 0.20F * level; // 20% total reduction per level
                float effectiveMult = mult * (1.0F - piercingPct);
                float finalMult = 1.0F - effectiveMult;
                if (finalMult < 0.0F) finalMult = 0.0F;
                dmg *= finalMult;
            }
        }

        return Math.max(0.0F, dmg);
    }

    private static final float DAMASK_REFLECT_CAP = 100.0F;
    private static final float DAMASK_THORNS_BONUS_CAP = 5.0F;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();


        if (event.getSource().getEntity() instanceof LivingEntity attacker && target.level() instanceof ServerLevel sl) {
            var piercingAttr = attacker.getAttribute(ModAttributes.ARMOR_PIERCING.get());
            if (piercingAttr != null && piercingAttr.getValue() > 0) {
                float piercingPct = (float) piercingAttr.getValue();

                float piercedDmg = computePiercedDamage(target, event.getSource(), event.getAmount(), piercingPct);

                CompoundTag tag = target.getPersistentData();
                tag.putFloat(TAG_OSMIUM_PIERCED_DMG, piercedDmg);
                tag.putLong(TAG_OSMIUM_PIERCED_TICK, sl.getGameTime());
            }
        }

        double negation = 0.0D;

        var negationAttr = target.getAttribute(ModAttributes.DAMAGE_NEGATION.get());
        if (negationAttr != null) {
            negation += negationAttr.getValue();
        }

        ItemStack chestItem = target.getItemBySlot(EquipmentSlot.CHEST);
        if (chestItem.is(ModItems.VANADIUM_HORSE_ARMOR.get())) {
            negation += 4.0D;
        }

        if (negation > 0) {
            if (event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK) || event.getSource().is(DamageTypes.GENERIC) || event.getSource().is(DamageTypes.ARROW) || event.getSource().is(DamageTypes.CACTUS) || event.getSource().is(DamageTypes.EXPLOSION) || event.getSource().is(DamageTypes.PLAYER_EXPLOSION) || event.getSource().is(DamageTypes.FALLING_ANVIL) || event.getSource().is(DamageTypes.FALLING_BLOCK) || event.getSource().is(DamageTypes.FALLING_STALACTITE) || event.getSource().is(DamageTypes.FLY_INTO_WALL) || event.getSource().is(DamageTypes.SWEET_BERRY_BUSH) || event.getSource().is(DamageTypes.STALAGMITE) || event.getSource().is(DamageTypes.HOT_FLOOR) || event.getSource().is(DamageTypes.BAD_RESPAWN_POINT) || event.getSource().is(DamageTypes.TRIDENT) || event.getSource().is(DamageTypes.MOB_PROJECTILE) || event.getSource().is(DamageTypes.THROWN)) {
                float newDamage = event.getAmount() - (float) negation;
                if (newDamage < 0) newDamage = 0;
                event.setAmount(newDamage);
            }
        }

        if (target.level() instanceof ServerLevel slLevel) {
            if (!event.getSource().is(DamageTypes.THORNS)) {
                double pct = getCounterThornsPctFromArmor(target);
                if (pct > 0.0D && isMeleeOrProjectile(event.getSource())) {
                    float preAbsorb = computeDamageBeforeAbsorption(target, event.getSource(), event.getAmount());
                    CompoundTag tag = target.getPersistentData();
                    tag.putFloat(TAG_DAMASK_PRE_ABSORB_DMG, preAbsorb);
                    tag.putLong(TAG_DAMASK_PRE_ABSORB_TICK, slLevel.getGameTime());
                }
            }
        }

        if (!(target instanceof Player player)) return;
        if (!(player.level() instanceof ServerLevel)) return;

        var reboundAttr = player.getAttribute(ModAttributes.DAMAGE_REBOUND.get());
        if (reboundAttr == null) return;

        double pct = reboundAttr.getValue();
        if (pct <= 0.0D) return;

        DamageSource src = event.getSource();
        if (!isHostileOrHostileProjectile(src)) return;

        float add = (float) (event.getAmount() * pct);
        if (add <= 0.0F) return;

        CompoundTag tag = player.getPersistentData();
        float stored = tag.getFloat(TAG_REBOUND);
        stored = Mth.clamp(stored + add, 0.0F, REBOUND_CAP);
        tag.putFloat(TAG_REBOUND, stored);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerSpendRebound(LivingHurtEvent event) {
        DamageSource src = event.getSource();
        Entity attacker = src.getEntity();

        if (!(attacker instanceof Player player)) return;
        if (!(player.level() instanceof ServerLevel)) return;
        if (!src.is(DamageTypes.PLAYER_ATTACK)) return;

        CompoundTag tag = player.getPersistentData();
        float stored = tag.getFloat(TAG_REBOUND);
        if (stored <= 0.0F) return;

        event.setAmount(event.getAmount() + stored);
        tag.putFloat(TAG_REBOUND, 0.0F);
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (!event.isVanillaCritical()) return;

        double bonus = 0.0D;
        var inst = player.getAttribute(ModAttributes.CRIT_DAMAGE_BONUS.get());
        if (inst != null) bonus = inst.getValue();

        event.setResult(Event.Result.ALLOW);
        event.setDamageModifier((float) (1.5F + bonus));
    }

    private static boolean isVanillaCritical(Player player, LivingEntity target) {
        if (player.isSprinting()) return false;
        if (player.isPassenger()) return false;
        if (player.onGround()) return false;
        if (player.fallDistance <= 0.0F) return false;
        if (player.onClimbable()) return false;
        if (player.isInWater() || player.isInLava()) return false;
        if (player.hasEffect(net.minecraft.world.effect.MobEffects.BLINDNESS)) return false;
        return target != null;
    }

    private static float rollThornsSynergyBonus(LivingEntity wearer) {
        RandomSource rand = wearer.getRandom();
        float bonus = 0.0F;
        EquipmentSlot[] slots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
        for (EquipmentSlot slot : slots) {
            ItemStack stack = wearer.getItemBySlot(slot);
            if (stack.isEmpty()) continue;
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.THORNS, stack);
            if (level <= 0) continue;
            float chance = 0.15F * level;
            if (rand.nextFloat() < chance) {
                float dmg = 1.0F + rand.nextFloat() * 4.0F;
                bonus += dmg;
                stack.hurtAndBreak(0, wearer, e -> e.broadcastBreakEvent(slot));
            }
        }
        return Math.min(bonus, DAMASK_THORNS_BONUS_CAP);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();

        if (target.level() instanceof ServerLevel sl) {
            CompoundTag tag = target.getPersistentData();
            if (tag.getLong(TAG_OSMIUM_PIERCED_TICK) == sl.getGameTime()) {
                float piercedDmg = tag.getFloat(TAG_OSMIUM_PIERCED_DMG);

                // If our pierced damage calculation is higher than what Vanilla decided, enforce ours!
                if (piercedDmg > event.getAmount()) {
                    event.setAmount(piercedDmg);
                }
            }
        }

        if (!event.getSource().is(DamageTypes.THORNS)) {
            double pct = getCounterThornsPctFromArmor(target);
            if (pct > 0.0D && isMeleeOrProjectile(event.getSource())) {
                Entity attackerEntity = event.getSource().getEntity();
                if (attackerEntity instanceof LivingEntity attacker && attacker != target) {
                    float finalDamageTaken = event.getAmount();

                    if (finalDamageTaken <= 0.0F && target.level() instanceof ServerLevel slLevel) {
                        CompoundTag tag = target.getPersistentData();
                        if (tag.getLong(TAG_DAMASK_PRE_ABSORB_TICK) == slLevel.getGameTime()) {
                            finalDamageTaken = tag.getFloat(TAG_DAMASK_PRE_ABSORB_DMG);
                        }
                    }

                    if (finalDamageTaken > 0.0F) {
                        float reflected = (float) (finalDamageTaken * pct);
                        MobEffectInstance str = target.getEffect(MobEffects.DAMAGE_BOOST);
                        if (str != null) {
                            int level = str.getAmplifier() + 1;
                            reflected += 1.0F * level;
                        }
                        reflected += rollThornsSynergyBonus(target);
                        reflected = Math.min(reflected, DAMASK_REFLECT_CAP);

                        if (reflected > 0.0F) {
                            var ds = target.damageSources().thorns(target);
                            if (ds.scalesWithDifficulty()) {
                                switch (target.level().getDifficulty()) {
                                    case EASY -> reflected /= 0.5F;
                                    case HARD -> reflected /= 1.5F;
                                    default -> { }
                                }
                            }
                            attacker.hurt(ds, reflected);
                        }
                    }
                }
            }
        }


        float shockAbsorption = 0.0F;
        var shockAttr = target.getAttribute(ModAttributes.SHOCK_ABSORPTION.get());
        if (shockAttr != null) {
            shockAbsorption += (float) shockAttr.getValue();
        }

        int osmiumPieces = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = target.getItemBySlot(slot);
            if (stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == ModArmorMaterials.OSMIUM) {
                osmiumPieces++;
            }
            if (slot == EquipmentSlot.CHEST && stack.is(ModItems.OSMIUM_HORSE_ARMOR.get())) {
                shockAbsorption += 0.40F;
            }
        }
        shockAbsorption += osmiumPieces * 0.15F;

        if (shockAbsorption > 0.0F) {
            shockAbsorption = Math.min(shockAbsorption, 0.9F);
            float maxHealth = target.getMaxHealth();
            float maxDamagePerHit = maxHealth * (1.0F - shockAbsorption);
            if (event.getAmount() > maxDamagePerHit) {
                event.setAmount(maxDamagePerHit);
            }
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack activeItem = player.getUseItem();
            if (activeItem.is(ModItems.BULWARK.get())) {
                if (event.getDamageSource().getDirectEntity() instanceof LivingEntity attacker) {
                    attacker.knockback(1.2D, player.getX() - attacker.getX(), player.getZ() - attacker.getZ());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (!event.getLevel().isClientSide()) {
            Map<Player, Vec3> hitPlayers = event.getExplosion().getHitPlayers();
            for (Map.Entry<Player, Vec3> entry : hitPlayers.entrySet()) {
                Player player = entry.getKey();
                boolean hasBulwark = player.getMainHandItem().is(ModItems.BULWARK.get()) || player.getOffhandItem().is(ModItems.BULWARK.get());
                if (hasBulwark) {
                    double multiplier = (player.isBlocking() && player.getUseItem().is(ModItems.BULWARK.get())) ? 0.05D : 0.60D;
                    Vec3 originalPush = entry.getValue();
                    Vec3 reducedPush = originalPush.scale(multiplier);
                    entry.setValue(reducedPush);
                    Vec3 difference = originalPush.subtract(reducedPush);
                    player.setDeltaMovement(player.getDeltaMovement().subtract(difference));
                    player.hurtMarked = true;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerKnockback(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isBlocking() && player.getUseItem().is(ModItems.BULWARK.get())) {
                event.setStrength(event.getStrength() * 0.05F);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            ItemStack activeItem = player.getUseItem();
            if (player.isBlocking() && activeItem.is(ModItems.BULWARK.get())) {
                DamageSource source = event.getSource();
                Vec3 sourcePos = source.getSourcePosition();
                if (sourcePos != null) {
                    Vec3 viewVec = player.getViewVector(1.0F);
                    Vec3 toSourceVec = sourcePos.vectorTo(player.position()).normalize();
                    toSourceVec = new Vec3(toSourceVec.x, 0.0D, toSourceVec.z);
                    double dot = toSourceVec.dot(viewVec);
                    if (dot >= 0.0D && dot < 0.5D) {
                        event.setCanceled(true);
                        player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + player.level().random.nextFloat() * 0.4F);
                        activeItem.hurtAndBreak((int) Math.max(1, event.getAmount()), player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                }
            }
        }
    }

    public static double getCounterThornsPctFromArmor(LivingEntity entity) {
        double total = 0.0D;
        EquipmentSlot[] armorSlots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.isEmpty()) continue;
            var mods = stack.getAttributeModifiers(slot).get(ModAttributes.COUNTER_THORNS.get());
            if (mods == null || mods.isEmpty()) continue;
            for (AttributeModifier mod : mods) {
                total += mod.getAmount();
            }
        }
        return Mth.clamp(total, 0.0D, 1.0D);
    }

    private static boolean isHostileOrHostileProjectile(DamageSource src) {
        Entity attacker = src.getEntity();
        if (attacker instanceof Monster) return true;
        Entity direct = src.getDirectEntity();
        if (direct instanceof Projectile proj) {
            Entity owner = proj.getOwner();
            return owner instanceof Monster;
        }
        return false;
    }

    private static boolean isMeleeOrProjectile(DamageSource src) {
        if (src.is(DamageTypes.PLAYER_ATTACK) || src.is(DamageTypes.MOB_ATTACK)) return true;
        if (src.is(DamageTypes.ARROW) || src.is(DamageTypes.TRIDENT) || src.is(DamageTypes.MOB_PROJECTILE) || src.is(DamageTypes.THROWN)) return true;
        return src.getDirectEntity() instanceof Projectile;
    }
}