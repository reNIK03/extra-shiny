package net.r_nik.extrashiny.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.ModModelLayers;
import net.r_nik.extrashiny.client.model.VanadiumGolemModel;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

public class VanadiumGolemRenderer
        extends MobRenderer<VanadiumGolemEntity, VanadiumGolemModel<VanadiumGolemEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ExtraShiny.MOD_ID, "textures/entity/vanadium_golem.png");

    private static final ResourceLocation HOSTILE_TEXTURE =
            new ResourceLocation(ExtraShiny.MOD_ID, "textures/entity/vanadium_golem_hostile.png");

    public VanadiumGolemRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new VanadiumGolemModel<>(context.bakeLayer(ModModelLayers.VANADIUM_GOLEM_MAIN)),
                1F
        );


        this.addLayer(new VanadiumGolemDecorLayer(this));
       // THIS IS PERMANENTLY NOT IMPLEMENTED
        // this.addLayer(new VanadiumGolemArmorLayer(this, context.getModelSet()));
        this.addLayer(new VanadiumGolemEyesLayer(this));

    }

    @Override
    public ResourceLocation getTextureLocation(VanadiumGolemEntity entity) {
        return entity.isHostileToPlayer()
                ? HOSTILE_TEXTURE
                : TEXTURE;
    }


    @Override
    public void render(
            VanadiumGolemEntity entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}