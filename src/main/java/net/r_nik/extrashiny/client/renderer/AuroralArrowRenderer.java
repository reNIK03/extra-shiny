package net.r_nik.extrashiny.client.renderer;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.entity.AuroralArrowEntity;

public class AuroralArrowRenderer extends ArrowRenderer<AuroralArrowEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ExtraShiny.MOD_ID, "textures/entity/auroral_arrow.png");

    public AuroralArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(AuroralArrowEntity entity) {
        return TEXTURE;
    }
}