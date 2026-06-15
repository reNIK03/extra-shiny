package net.r_nik.extrashiny.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.r_nik.extrashiny.client.ModBlockEntityWithoutLevelRenderer;

import java.util.UUID;
import java.util.function.Consumer;

public class BulwarkItem extends ShieldItem {
    private static final UUID MAINHAND_KB_UUID = UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6A31");
    private static final UUID MAINHAND_SPEED_UUID = UUID.fromString("B243B127-1823-45C6-8912-3E6B2A8C1032");

    private static final UUID OFFHAND_KB_UUID = UUID.fromString("C3D4E5F6-1234-5678-9ABC-DEF012345678");
    private static final UUID OFFHAND_SPEED_UUID = UUID.fromString("F6E5D4C3-8765-4321-CBA9-876543210FED");

    public BulwarkItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(MAINHAND_KB_UUID,
                    "Bulwark KB resistance", 0.4D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MAINHAND_SPEED_UUID,
                    "Bulwark slowness", -0.30D, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }
        else if (slot == EquipmentSlot.OFFHAND) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(OFFHAND_KB_UUID,
                    "Bulwark KB resistance", 0.4D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(OFFHAND_SPEED_UUID,
                    "Bulwark slowness", -0.30D, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        }

        return super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        if (pRepair.is(ModItems.OSMIUM_INGOT.get())) {
            return true;
        }
        return super.isValidRepairItem(pToRepair, pRepair);
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
}