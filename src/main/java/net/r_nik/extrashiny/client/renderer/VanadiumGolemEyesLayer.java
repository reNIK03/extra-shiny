package net.r_nik.extrashiny.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.model.VanadiumGolemModel;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class VanadiumGolemEyesLayer
        extends RenderLayer<VanadiumGolemEntity, VanadiumGolemModel<VanadiumGolemEntity>> {

    private static final ResourceLocation NORMAL_EYES =
            new ResourceLocation(
                    ExtraShiny.MOD_ID,
                    "textures/entity/vanadium_golem_eyes.png"
            );

    private static final ResourceLocation HOSTILE_EYES =
            new ResourceLocation(
                    ExtraShiny.MOD_ID,
                    "textures/entity/vanadium_golem_eyes_hostile.png"
            );

    public VanadiumGolemEyesLayer(
            RenderLayerParent<
                    VanadiumGolemEntity,
                    VanadiumGolemModel<VanadiumGolemEntity>> parent
    ) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            VanadiumGolemEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        ResourceLocation eyesTexture =
                entity.isHostileToPlayer()
                        ? HOSTILE_EYES
                        : NORMAL_EYES;

        this.getParentModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(RenderType.eyes(eyesTexture)),
                15728640,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                1.0F
        );
    }
}
