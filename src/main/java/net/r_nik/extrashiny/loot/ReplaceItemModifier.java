package net.r_nik.extrashiny.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ReplaceItemModifier extends LootModifier {
    public static final Supplier<Codec<ReplaceItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst).and(
                    inst.group(
                            ForgeRegistries.ITEMS.getCodec().fieldOf("target").forGetter(m -> m.targetItem),
                            ForgeRegistries.ITEMS.getCodec().fieldOf("replacement").forGetter(m -> m.replacementItem)
                    )
            ).apply(inst, ReplaceItemModifier::new))
    );

    private final Item targetItem;
    private final Item replacementItem;

    public ReplaceItemModifier(LootItemCondition[] conditionsIn, Item targetItem, Item replacementItem) {
        super(conditionsIn);
        this.targetItem = targetItem;
        this.replacementItem = replacementItem;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack stack = generatedLoot.get(i);

            if (stack.is(targetItem)) {

                generatedLoot.set(i, new ItemStack(replacementItem, stack.getCount()));
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}