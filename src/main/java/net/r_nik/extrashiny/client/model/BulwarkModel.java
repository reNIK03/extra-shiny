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

public class BulwarkModel extends Model {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ExtraShiny.MOD_ID, "bulwark"), "main");
    private final ModelPart plate;
    private final ModelPart handle;

    public BulwarkModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.plate = root.getChild("plate");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition plate = partdefinition.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -30.0F, -1.0F, 12.0F, 30.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(-14.0F, -27.0F, -1.0F, 3.0F, 21.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 23).addBox(1.0F, -27.0F, -1.0F, 3.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 21.0F, 0.0F));

        PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -19.0F, 1.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 21.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        plate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}