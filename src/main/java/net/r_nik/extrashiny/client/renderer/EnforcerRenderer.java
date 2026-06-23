package net.r_nik.extrashiny.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.ModModelLayers;
import net.r_nik.extrashiny.client.model.EnforcerModel;
import net.r_nik.extrashiny.entity.EnforcerEntity;

public class EnforcerRenderer extends MobRenderer<EnforcerEntity, EnforcerModel<EnforcerEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "textures/entity/enforcer.png");

    public EnforcerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EnforcerModel<>(ctx.bakeLayer(ModModelLayers.ENFORCER_MAIN)), 0.6F);

        this.addLayer(new EnforcerGlowLayer<>(this));
        this.addLayer(new EnforcerPulseLayer<>(this));
    }

    public class EnforcerGlowLayer<T extends EnforcerEntity, M extends EntityModel<T>>
            extends RenderLayer<T, M> {

        private static final ResourceLocation GLOW =
                ResourceLocation.fromNamespaceAndPath("extrashiny", "textures/entity/enforcer_glow.png");

        public EnforcerGlowLayer(RenderLayerParent<T, M> parent) {
            super(parent);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                           T entity, float limbSwing, float limbSwingAmount, float partialTick,
                           float ageInTicks, float netHeadYaw, float headPitch) {

            VertexConsumer vc = buffer.getBuffer(RenderType.eyes(GLOW));
            int fullBright = 0xF000F0;

            float glow = 0.15F;
            // Pack the color: A=255 (opaque), R, G, B = glow * 255
            int packedColor = FastColor.ARGB32.color(255, (int)(glow * 255), (int)(glow * 255), (int)(glow * 255));

            this.getParentModel().renderToBuffer(
                    poseStack, vc, fullBright,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                    packedColor
            );
        }
    }

    public class EnforcerPulseLayer<T extends EnforcerEntity, M extends EntityModel<T>>
            extends RenderLayer<T, M> {

        private static final ResourceLocation PULSE =
                ResourceLocation.fromNamespaceAndPath("extrashiny", "textures/entity/enforcer_pulse.png");
        private static final float PULSE_MAX_TICKS = 18.0F;

        public EnforcerPulseLayer(RenderLayerParent<T, M> parent) {
            super(parent);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                           T entity, float limbSwing, float limbSwingAmount, float partialTick,
                           float ageInTicks, float netHeadYaw, float headPitch) {

            int ticks = entity.getSignalTicks();
            if (ticks <= 0) return;

            float alpha = (ticks - partialTick) / PULSE_MAX_TICKS;
            alpha = Mth.clamp(alpha, 0.0F, 1.0F);

            VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentEmissive(PULSE));
            int fullBright = 0xF000F0;

            // Pack the color: A = alpha * 255, R,G,B = 255 (white)
            int packedColor = FastColor.ARGB32.color((int)(alpha * 255), 255, 255, 255);

            this.getParentModel().renderToBuffer(
                    poseStack, vc, fullBright,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                    packedColor
            );
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EnforcerEntity entity) {
        return TEXTURE;
    }
}