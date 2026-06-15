package net.r_nik.extrashiny.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.r_nik.extrashiny.attribute.ModAttributes;
import net.r_nik.extrashiny.client.model.CimmerianArmorModel;

import java.util.UUID;
import java.util.function.Consumer;

public class CimmerianArmorItem extends ArmorItem {

    private static final double REBOUND_PER_PIECE = 0.125D;

    private static final UUID REBOUND_HELMET_UUID = UUID.fromString("7b7b6fb7-5c4f-4c1e-9d7a-9c2d4e1f1a01");
    private static final UUID REBOUND_CHEST_UUID  = UUID.fromString("7b7b6fb7-5c4f-4c1e-9d7a-9c2d4e1f1a02");
    private static final UUID REBOUND_LEGS_UUID   = UUID.fromString("7b7b6fb7-5c4f-4c1e-9d7a-9c2d4e1f1a03");
    private static final UUID REBOUND_BOOTS_UUID  = UUID.fromString("7b7b6fb7-5c4f-4c1e-9d7a-9c2d4e1f1a04");

    public CimmerianArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot, stack));

        if (slot == this.getEquipmentSlot()) {
            builder.put(
                    ModAttributes.DAMAGE_REBOUND.get(),
                    new AttributeModifier(getReboundUuid(slot), "Damage rebound", REBOUND_PER_PIECE, AttributeModifier.Operation.ADDITION)
            );
            return builder.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    private static UUID getReboundUuid(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> REBOUND_HELMET_UUID;
            case CHEST -> REBOUND_CHEST_UUID;
            case LEGS -> REBOUND_LEGS_UUID;
            case FEET -> REBOUND_BOOTS_UUID;
            default -> REBOUND_CHEST_UUID;
        };
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
