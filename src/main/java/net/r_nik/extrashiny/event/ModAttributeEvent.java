package net.r_nik.extrashiny.event;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.attribute.ModAttributes;

// 1.21.1: Removed 'bus' as it is deprecated and automatic
@EventBusSubscriber(modid = ExtraShiny.MOD_ID)
public class ModAttributeEvent {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        // Player Attributes
        event.add(EntityType.PLAYER, ModAttributes.DAMAGE_NEGATION, 0.0D);
        event.add(EntityType.PLAYER, ModAttributes.SHOCK_ABSORPTION, 0.0D);
        event.add(EntityType.PLAYER, ModAttributes.DAMAGE_REBOUND, 0.0D);
        event.add(EntityType.PLAYER, ModAttributes.COUNTER_THORNS, 0.0D);
        event.add(EntityType.PLAYER, ModAttributes.CRIT_DAMAGE_BONUS, 0.0D);
        event.add(EntityType.PLAYER, ModAttributes.ARMOR_PIERCING, 0.0D);

        // Zombie-type Attributes
        event.add(EntityType.ZOMBIE, ModAttributes.COUNTER_THORNS, 0.0D);
        event.add(EntityType.HUSK, ModAttributes.COUNTER_THORNS, 0.0D);
        event.add(EntityType.DROWNED, ModAttributes.COUNTER_THORNS, 0.0D);
        event.add(EntityType.ZOMBIE_VILLAGER, ModAttributes.COUNTER_THORNS, 0.0D);

        // Skeleton-type Attributes
        event.add(EntityType.SKELETON, ModAttributes.COUNTER_THORNS, 0.0D);
        event.add(EntityType.STRAY, ModAttributes.COUNTER_THORNS, 0.0D);
        event.add(EntityType.WITHER_SKELETON, ModAttributes.COUNTER_THORNS, 0.0D);

        // Piglin-type Attributes
        event.add(EntityType.PIGLIN, ModAttributes.COUNTER_THORNS, 0.0D);
        event.add(EntityType.PIGLIN_BRUTE, ModAttributes.COUNTER_THORNS, 0.0D);
    }
}