package net.r_nik.extrashiny.client.model;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class CimmerianArmorModel<T extends LivingEntity> extends HumanoidArmorModel<T> {

    public static final CimmerianArmorModel<?> OUTER =
            new CimmerianArmorModel<>(
                    createLayerDefinition(LayerDefinitions.OUTER_ARMOR_DEFORMATION).bakeRoot()
            );

    public static final CimmerianArmorModel<?> INNER =
            new CimmerianArmorModel<>(
                    createLayerDefinition(LayerDefinitions.INNER_ARMOR_DEFORMATION).bakeRoot()
            );

    public CimmerianArmorModel(ModelPart root) {
        super(root);
    }

    public static MeshDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = HumanoidArmorModel.createBodyLayer(deformation);
        PartDefinition root = mesh.getRoot();

        boolean outer = deformation == LayerDefinitions.OUTER_ARMOR_DEFORMATION;

        float pillarInflateX = outer ? 0.25F : 0.10F;
        float pillarInflateY = outer ? 0.75F : 0.25F;
        float pillarInflateZ = outer ? 0.25F : 0.10F;

        float topInflateX    = outer ? 0.50F : 0.15F;
        float topInflateY    = outer ? 0.25F : 0.05F;
        float topInflateZ    = outer ? 0.25F : 0.15F;

        CubeDeformation crestPillar = new CubeDeformation(
                pillarInflateX,
                pillarInflateY,
                pillarInflateZ
        );

        CubeDeformation crestTop = new CubeDeformation(
                topInflateX,
                topInflateY,
                topInflateZ
        );

        float pillarXOffset = pillarInflateX * 0.5F;
        float topXOffset    = topInflateX * 0.5F;

        PartDefinition head = root.getChild("head");

        head.addOrReplaceChild(
                "crest_left_pillar",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -7.12F - pillarXOffset,
                                -10.75F,
                                -1.0F,
                                2.0F, 6.0F, 2.0F,
                                crestPillar
                        )
                ,
                PartPose.ZERO
        );

        head.addOrReplaceChild(
                "crest_top_left",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(
                                -4.25F - topXOffset,
                                -11.25F,
                                -1.0F,
                                4.0F, 2.0F, 2.0F,
                                crestTop
                        )
                ,
                PartPose.ZERO
        );

        head.addOrReplaceChild(
                "crest_top_right",
                CubeListBuilder.create()
                        .texOffs(26, 4)
                        .addBox(
                                0.25F + topXOffset,
                                -11.25F,
                                -1.0F,
                                4.0F, 2.0F, 2.0F,
                                crestTop
                        )
                ,
                PartPose.ZERO
        );

        head.addOrReplaceChild(
                "crest_right_pillar",
                CubeListBuilder.create()
                        .texOffs(56, 0)
                        .addBox(
                                5.12F + pillarXOffset,
                                -10.75F,
                                -1.0F,
                                2.0F, 6.0F, 2.0F,
                                crestPillar
                        )
                ,
                PartPose.ZERO
        );

        return mesh;
    }

    public static LayerDefinition createLayerDefinition(CubeDeformation deformation) {
        return LayerDefinition.create(createBodyLayer(deformation), 64, 32);
    }
}
