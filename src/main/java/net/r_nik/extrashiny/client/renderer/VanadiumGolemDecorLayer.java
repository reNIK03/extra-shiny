package net.r_nik.extrashiny.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.model.VanadiumGolemModel;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;

import java.util.Map;

public class VanadiumGolemDecorLayer
        extends RenderLayer<VanadiumGolemEntity, VanadiumGolemModel<VanadiumGolemEntity>> {

    private static final Map<VanadiumGolemEntity.DecorType, ResourceLocation> DECOR_TEXTURES =
            Map.ofEntries(
                    Map.entry(VanadiumGolemEntity.DecorType.WHITE, decor("white")),
                    Map.entry(VanadiumGolemEntity.DecorType.ORANGE, decor("orange")),
                    Map.entry(VanadiumGolemEntity.DecorType.MAGENTA, decor("magenta")),
                    Map.entry(VanadiumGolemEntity.DecorType.LIGHT_BLUE, decor("light_blue")),
                    Map.entry(VanadiumGolemEntity.DecorType.YELLOW, decor("yellow")),
                    Map.entry(VanadiumGolemEntity.DecorType.LIME, decor("lime")),
                    Map.entry(VanadiumGolemEntity.DecorType.PINK, decor("pink")),
                    Map.entry(VanadiumGolemEntity.DecorType.GRAY, decor("gray")),
                    Map.entry(VanadiumGolemEntity.DecorType.LIGHT_GRAY, decor("light_gray")),
                    Map.entry(VanadiumGolemEntity.DecorType.CYAN, decor("cyan")),
                    Map.entry(VanadiumGolemEntity.DecorType.PURPLE, decor("purple")),
                    Map.entry(VanadiumGolemEntity.DecorType.BLUE, decor("blue")),
                    Map.entry(VanadiumGolemEntity.DecorType.BROWN, decor("brown")),
                    Map.entry(VanadiumGolemEntity.DecorType.GREEN, decor("green")),
                    Map.entry(VanadiumGolemEntity.DecorType.RED, decor("red")),
                    Map.entry(VanadiumGolemEntity.DecorType.BLACK, decor("black")),

                    Map.entry(VanadiumGolemEntity.DecorType.AMBER, decor("amber")),
                    Map.entry(VanadiumGolemEntity.DecorType.AQUA, decor("aqua")),
                    Map.entry(VanadiumGolemEntity.DecorType.BEIGE, decor("beige")),
                    Map.entry(VanadiumGolemEntity.DecorType.CORAL, decor("coral")),
                    Map.entry(VanadiumGolemEntity.DecorType.FOREST, decor("forest")),
                    Map.entry(VanadiumGolemEntity.DecorType.GINGER, decor("ginger")),
                    Map.entry(VanadiumGolemEntity.DecorType.INDIGO, decor("indigo")),
                    Map.entry(VanadiumGolemEntity.DecorType.MAROON, decor("maroon")),
                    Map.entry(VanadiumGolemEntity.DecorType.MINT, decor("mint")),
                    Map.entry(VanadiumGolemEntity.DecorType.NAVY, decor("navy")),
                    Map.entry(VanadiumGolemEntity.DecorType.OLIVE, decor("olive")),
                    Map.entry(VanadiumGolemEntity.DecorType.ROSE, decor("rose")),
                    Map.entry(VanadiumGolemEntity.DecorType.SLATE, decor("slate")),
                    Map.entry(VanadiumGolemEntity.DecorType.TAN, decor("tan")),
                    Map.entry(VanadiumGolemEntity.DecorType.TEAL, decor("teal")),
                    Map.entry(VanadiumGolemEntity.DecorType.VERDANT, decor("verdant"))
            );

    private static ResourceLocation decor(String name) {
        return ResourceLocation.fromNamespaceAndPath(
                ExtraShiny.MOD_ID,
                "textures/entity/decor/" + name + ".png"
        );
    }

    public VanadiumGolemDecorLayer(
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
            VanadiumGolemEntity golem,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!golem.hasDecor()) return;

        ResourceLocation texture = DECOR_TEXTURES.get(golem.getDecor());
        if (texture == null) return;

        // Use 0xFFFFFFFF (White) as the packedColor to replace the three 1.0F arguments
        renderColoredCutoutModel(
                this.getParentModel(),
                texture,
                poseStack,
                buffer,
                packedLight,
                golem,
                0xFFFFFFFF
        );
    }
}