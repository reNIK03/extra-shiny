package net.r_nik.extrashiny.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.r_nik.extrashiny.ExtraShiny;

public class VanadiumPartisanModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ExtraShiny.res("vanadium_partisan"), "main");
    private final ModelPart bone;

    public VanadiumPartisanModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(5, 6).addBox(-0.5F, 4.0F, -0.5F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(17, -5).addBox(0.0F, -5.0F, -3.5F, 0.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }



    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}