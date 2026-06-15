package net.r_nik.extrashiny.event;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.r_nik.extrashiny.attribute.ModAttributes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributeEvent {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.DAMAGE_NEGATION.get());
        event.add(EntityType.PLAYER, ModAttributes.SHOCK_ABSORPTION.get());
        event.add(EntityType.PLAYER, ModAttributes.DAMAGE_REBOUND.get());
        event.add(EntityType.PLAYER, ModAttributes.COUNTER_THORNS.get());

        event.add(EntityType.ZOMBIE, ModAttributes.COUNTER_THORNS.get());
        event.add(EntityType.HUSK, ModAttributes.COUNTER_THORNS.get());
        event.add(EntityType.DROWNED, ModAttributes.COUNTER_THORNS.get());
        event.add(EntityType.ZOMBIE_VILLAGER, ModAttributes.COUNTER_THORNS.get());

        event.add(EntityType.SKELETON, ModAttributes.COUNTER_THORNS.get());
        event.add(EntityType.STRAY, ModAttributes.COUNTER_THORNS.get());
        event.add(EntityType.WITHER_SKELETON, ModAttributes.COUNTER_THORNS.get());

        event.add(EntityType.PIGLIN, ModAttributes.COUNTER_THORNS.get());
        event.add(EntityType.PIGLIN_BRUTE, ModAttributes.COUNTER_THORNS.get());

        event.add(EntityType.PLAYER, ModAttributes.CRIT_DAMAGE_BONUS.get());
        event.add(EntityType.PLAYER, ModAttributes.ARMOR_PIERCING.get());
    }
}