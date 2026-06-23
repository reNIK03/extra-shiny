package net.r_nik.extrashiny.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.model.HumanoidModel;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.attribute.ModAttributes;
import net.r_nik.extrashiny.client.model.DamaskArmorModel;

import java.util.function.Consumer;

public class DamaskArmorItem extends ArmorItem {

    private static final ResourceLocation CT_HELMET_ID = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "counter_thorns_helmet");
    private static final ResourceLocation CT_CHEST_ID  = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "counter_thorns_chest");
    private static final ResourceLocation CT_LEGS_ID   = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "counter_thorns_legs");
    private static final ResourceLocation CT_BOOTS_ID  = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "counter_thorns_boots");

    public DamaskArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers base = super.getDefaultAttributeModifiers(stack);

        EquipmentSlot slot = this.getEquipmentSlot();
        double amount = getCounterThornsAmount(slot);

        if (amount > 0.0D) {
            return base.withModifierAdded(
                    ModAttributes.COUNTER_THORNS,
                    new AttributeModifier(getCounterThornsId(slot), amount, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.bySlot(slot)
            );
        }

        return base;
    }

    private static double getCounterThornsAmount(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD  -> 0.15D;
            case CHEST -> 0.40D;
            case LEGS  -> 0.30D;
            case FEET  -> 0.15D;
            default    -> 0.0D;
        };
    }

    private static ResourceLocation getCounterThornsId(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> CT_HELMET_ID;
            case CHEST -> CT_CHEST_ID;
            case LEGS -> CT_LEGS_ID;
            case FEET -> CT_BOOTS_ID;
            default -> CT_CHEST_ID;
        };
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(
                    LivingEntity entity,
                    ItemStack stack,
                    EquipmentSlot slot,
                    HumanoidModel<?> original
            ) {
                return slot == EquipmentSlot.LEGS ? DamaskArmorModel.INNER : DamaskArmorModel.OUTER;
            }
        });
    }
}