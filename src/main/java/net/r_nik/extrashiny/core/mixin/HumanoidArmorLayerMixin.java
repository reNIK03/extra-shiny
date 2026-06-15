package net.r_nik.extrashiny.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.r_nik.extrashiny.item.ModItems;
import net.r_nik.extrashiny.trim.ExtraShinyTrimPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
        extends RenderLayer<T, M> {

    protected HumanoidArmorLayerMixin(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    @Shadow
    protected abstract void renderTrim(ArmorMaterial material, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                       ArmorTrim trim, Model model, boolean innerModel);

    @Shadow
    protected abstract Model getArmorModelHook(T entity, ItemStack stack, EquipmentSlot slot, A model);

    @Inject(
            method = "renderArmorPiece",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasFoil()Z")
    )
    private void extrashiny$renderForcedHelmetTrimPattern(
            PoseStack poseStack,
            MultiBufferSource buffer,
            T entity,
            EquipmentSlot slot,
            int packedLight,
            A model,
            CallbackInfo ci
    ) {
        if (slot != EquipmentSlot.HEAD) return;

        ItemStack stack = entity.getItemBySlot(slot);
        if (!(stack.getItem() instanceof ArmorItem armorItem)) return;

        var forcedPatternKey =
                stack.is(ModItems.CIMMERIAN_HELMET.get()) ? ExtraShinyTrimPatterns.CIMMERIAN :
                        stack.is(ModItems.DAMASK_HELMET.get())    ? ExtraShinyTrimPatterns.DAMASK :
                                null;

        if (forcedPatternKey == null) return;

        RegistryAccess access = entity.level().registryAccess();

        ArmorTrim.getTrim(access, stack).ifPresent(existingTrim -> {
            ArmorTrim forcedPatternTrim = new ArmorTrim(
                    existingTrim.material(),
                    access.registryOrThrow(Registries.TRIM_PATTERN).getHolderOrThrow(forcedPatternKey)
            );

            this.renderTrim(
                    armorItem.getMaterial(),
                    poseStack,
                    buffer,
                    packedLight,
                    forcedPatternTrim,
                    this.getArmorModelHook(entity, stack, slot, model),
                    this.usesInnerModel(slot)
            );
        });
    }
}
