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
import net.r_nik.extrashiny.client.model.CimmerianArmorModel;

import java.util.function.Consumer;

public class CimmerianArmorItem extends ArmorItem {

    private static final double REBOUND_PER_PIECE = 0.125D;

    private static final ResourceLocation REBOUND_HELMET_ID = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "rebound_helmet");
    private static final ResourceLocation REBOUND_CHEST_ID  = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "rebound_chest");
    private static final ResourceLocation REBOUND_LEGS_ID   = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "rebound_legs");
    private static final ResourceLocation REBOUND_BOOTS_ID  = ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "rebound_boots");

    public CimmerianArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers base = super.getDefaultAttributeModifiers(stack);

        EquipmentSlot slot = this.getEquipmentSlot();
        return base.withModifierAdded(
                ModAttributes.DAMAGE_REBOUND,
                new AttributeModifier(getReboundId(slot), REBOUND_PER_PIECE, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.bySlot(slot)
        );
    }

    private static ResourceLocation getReboundId(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> REBOUND_HELMET_ID;
            case CHEST -> REBOUND_CHEST_ID;
            case LEGS -> REBOUND_LEGS_ID;
            case FEET -> REBOUND_BOOTS_ID;
            default -> REBOUND_CHEST_ID;
        };
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(
                    LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original
            ) {
                return slot == EquipmentSlot.LEGS ? CimmerianArmorModel.INNER : CimmerianArmorModel.OUTER;
            }
        });
    }
}