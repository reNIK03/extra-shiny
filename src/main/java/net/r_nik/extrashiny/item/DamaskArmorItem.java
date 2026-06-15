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
import net.r_nik.extrashiny.client.model.DamaskArmorModel;

import java.util.UUID;
import java.util.function.Consumer;

public class DamaskArmorItem extends ArmorItem {

    private static final UUID CT_HELMET_UUID = UUID.fromString("2a6d5d7e-0d4f-4f5b-9c4d-0c8f1b000101");
    private static final UUID CT_CHEST_UUID  = UUID.fromString("2a6d5d7e-0d4f-4f5b-9c4d-0c8f1b000102");
    private static final UUID CT_LEGS_UUID   = UUID.fromString("2a6d5d7e-0d4f-4f5b-9c4d-0c8f1b000103");
    private static final UUID CT_BOOTS_UUID  = UUID.fromString("2a6d5d7e-0d4f-4f5b-9c4d-0c8f1b000104");

    public DamaskArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot, stack));

        if (slot == this.getEquipmentSlot()) {
            double amount = getCounterThornsAmount(slot);

            if (amount > 0.0D) {
                builder.put(
                        ModAttributes.COUNTER_THORNS.get(),
                        new AttributeModifier(
                                getCounterThornsUuid(slot),
                                "Counter thorns",
                                amount,
                                AttributeModifier.Operation.ADDITION
                        )
                );
            }
            return builder.build();
        }

        return super.getAttributeModifiers(slot, stack);
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

    private static UUID getCounterThornsUuid(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> CT_HELMET_UUID;
            case CHEST -> CT_CHEST_UUID;
            case LEGS -> CT_LEGS_UUID;
            case FEET -> CT_BOOTS_UUID;
            default -> CT_CHEST_UUID;
        };
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
