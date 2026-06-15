package net.r_nik.extrashiny.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.r_nik.extrashiny.client.model.VanadiumGolemModel;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

// IF YOU READ THIS, THIS IS COMPLETELY SCRAPPED
// THEY WILL NOT WEAR HELMETS AT ALL (might be too much also)


public class VanadiumGolemArmorLayer
        extends RenderLayer<VanadiumGolemEntity, VanadiumGolemModel<VanadiumGolemEntity>> {

    private final HumanoidModel<LivingEntity> innerArmorModel;
    private final HumanoidModel<LivingEntity> outerArmorModel;
    private final TextureAtlas armorTrimAtlas;

    public VanadiumGolemArmorLayer(
            RenderLayerParent<VanadiumGolemEntity, VanadiumGolemModel<VanadiumGolemEntity>> parent,
            EntityModelSet modelSet
    ) {
        super(parent);

        this.innerArmorModel = new HumanoidModel<>(
                modelSet.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)
        );
        this.outerArmorModel = new HumanoidModel<>(
                modelSet.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)
        );

        this.armorTrimAtlas = net.minecraft.client.Minecraft.getInstance()
                .getModelManager()
                .getAtlas(net.minecraft.client.renderer.Sheets.ARMOR_TRIMS_SHEET);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            VanadiumGolemEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        renderHelmet(poseStack, buffer, packedLight, entity);
    }

    private void renderHelmet(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            VanadiumGolemEntity entity
    ) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (!(stack.getItem() instanceof ArmorItem armorItem)) return;

        poseStack.pushPose();

        ((VanadiumGolemModel<?>) getParentModel()).applyHeadTransform(poseStack);
        poseStack.translate(0.0F, -0.66F, 0.0F);
        poseStack.scale(1.15F, 1.15F, 1.15F);

        renderHelmetPass(entity, stack, armorItem, poseStack, buffer, light, innerArmorModel, false);
        renderHelmetPass(entity, stack, armorItem, poseStack, buffer, light, outerArmorModel, true);

        poseStack.popPose();
    }

    private void renderHelmetPass(
            VanadiumGolemEntity entity,
            ItemStack stack,
            ArmorItem armorItem,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            HumanoidModel<LivingEntity> model,
            boolean outer
    ) {
        Model armorModel = ForgeHooksClient.getArmorModel(
                entity,
                stack,
                EquipmentSlot.HEAD,
                model
        );

        ResourceLocation texture = getArmorResource(entity, stack, EquipmentSlot.HEAD, null);
        VertexConsumer baseConsumer =
                buffer.getBuffer(RenderType.armorCutoutNoCull(texture));

        model.setAllVisible(false);

        model.head.visible = true;
        armorModel.renderToBuffer(poseStack, baseConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        model.head.visible = true;
        model.hat.visible = true;
        armorModel.renderToBuffer(poseStack, baseConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        if (armorItem instanceof net.minecraft.world.item.DyeableLeatherItem dyeable) {
            ResourceLocation overlay =
                    getArmorResource(entity, stack, EquipmentSlot.HEAD, "overlay");

            int color = dyeable.getColor(stack);
            float r = (color >> 16 & 255) / 255F;
            float g = (color >> 8 & 255) / 255F;
            float b = (color & 255) / 255F;

            VertexConsumer overlayConsumer =
                    buffer.getBuffer(RenderType.armorCutoutNoCull(overlay));

            model.setAllVisible(false);
            model.head.visible = true;
            armorModel.renderToBuffer(poseStack, overlayConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 1F);

            model.head.visible = true;
            model.hat.visible = true;
            armorModel.renderToBuffer(poseStack, overlayConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
        }

        net.minecraft.world.item.armortrim.ArmorTrim.getTrim(
                entity.level().registryAccess(),
                stack
        ).ifPresent(trim -> {

            VertexConsumer trimConsumer =
                    armorTrimAtlas
                            .getSprite(
                                    outer
                                            ? trim.outerTexture(armorItem.getMaterial())
                                            : trim.innerTexture(armorItem.getMaterial())
                            )
                            .wrap(buffer.getBuffer(
                                    net.minecraft.client.renderer.Sheets.armorTrimsSheet()
                            ));

            model.setAllVisible(false);
            model.head.visible = true;
            armorModel.renderToBuffer(poseStack, trimConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

            model.head.visible = true;
            model.hat.visible = true;
            armorModel.renderToBuffer(poseStack, trimConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        });

        if (stack.hasFoil()) {
            VertexConsumer glintConsumer =
                    buffer.getBuffer(RenderType.armorEntityGlint());

            model.setAllVisible(false);
            model.head.visible = true;
            armorModel.renderToBuffer(poseStack, glintConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

            model.head.visible = true;
            model.hat.visible = true;
            armorModel.renderToBuffer(poseStack, glintConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
    }

    private ResourceLocation getArmorResource(
            VanadiumGolemEntity entity,
            ItemStack stack,
            EquipmentSlot slot,
            String type
    ) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";

        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }

        String path = String.format(
                "%s:textures/models/armor/%s_layer_1%s.png",
                domain,
                texture,
                type == null ? "" : "_" + type
        );

        path = ForgeHooksClient.getArmorTexture(entity, stack, path, slot, type);
        return new ResourceLocation(path);
    }
}
