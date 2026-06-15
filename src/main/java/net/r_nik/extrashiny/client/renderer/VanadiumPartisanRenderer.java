package net.r_nik.extrashiny.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer; // <-- ADD THIS IMPORT
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.model.VanadiumPartisanModel;
import net.r_nik.extrashiny.entity.VanadiumPartisanEntity;

public class VanadiumPartisanRenderer extends EntityRenderer<VanadiumPartisanEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ExtraShiny.MOD_ID, "textures/entity/vanadium_partisan.png");

    private final VanadiumPartisanModel model;

    public VanadiumPartisanRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new VanadiumPartisanModel(ctx.bakeLayer(VanadiumPartisanModel.LAYER_LOCATION));
    }

    @Override
    public void render(VanadiumPartisanEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(
                Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90F
        ));
        poseStack.mulPose(Axis.ZP.rotationDegrees(
                Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90F
        ));
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(
                buffer,
                model.renderType(getTextureLocation(entity)),
                false,
                entity.isFoil()
        );

        model.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f
        );

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(VanadiumPartisanEntity entity) {
        return TEXTURE;
    }
}