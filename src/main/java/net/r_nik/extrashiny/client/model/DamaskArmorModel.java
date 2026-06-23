package net.r_nik.extrashiny.client.model;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.world.entity.LivingEntity;

public class DamaskArmorModel<T extends LivingEntity> extends HumanoidArmorModel<T> {

    public static final DamaskArmorModel<?> OUTER =
            new DamaskArmorModel<>(
                    createLayerDefinition(LayerDefinitions.OUTER_ARMOR_DEFORMATION).bakeRoot()
            );

    public static final DamaskArmorModel<?> INNER =
            new DamaskArmorModel<>(
                    createLayerDefinition(LayerDefinitions.INNER_ARMOR_DEFORMATION).bakeRoot()
            );

    public DamaskArmorModel(ModelPart root) {
        super(root);
    }

    public static MeshDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = HumanoidArmorModel.createBodyLayer(deformation);
        PartDefinition root = mesh.getRoot();

        boolean outer = deformation == LayerDefinitions.OUTER_ARMOR_DEFORMATION;

        float largeInflateX = outer ? 0.50F : 0.12F;
        float largeInflateY = outer ? 0.50F : 0.12F;
        float largeInflateZ = outer ? 0.50F : 0.12F;

        float hornInflateX  = outer ? 0.375F : 0.08F;
        float hornInflateY  = outer ? 0.25F : 0.06F;
        float hornInflateZ  = outer ? 0.25F : 0.08F;

        CubeDeformation largeDeform = new CubeDeformation(
                largeInflateX, largeInflateY, largeInflateZ
        );

        CubeDeformation hornDeform = new CubeDeformation(
                hornInflateX, hornInflateY, hornInflateZ
        );

        float largeXOffset = largeInflateX * 0.5F;
        float hornXOffset  = hornInflateX  * 0.5F;

        PartDefinition head = root.getChild("head");

        head.addOrReplaceChild(
                "damask_side_left",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(
                                -9.25F - largeXOffset,
                                -7.25F,
                                -2.0F,
                                4.0F, 4.0F, 4.0F,
                                largeDeform
                        ),
                PartPose.ZERO
        );

        head.addOrReplaceChild(
                "damask_side_right",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(
                                5.25F + largeXOffset,
                                -7.25F,
                                -2.0F,
                                4.0F, 4.0F, 4.0F,
                                largeDeform
                        ),
                PartPose.ZERO
        );

        head.addOrReplaceChild(
                "damask_horn_left",
                CubeListBuilder.create()
                        .texOffs(54, 16)
                        .addBox(
                                -11.9375F - hornXOffset,
                                -5.0F,
                                -4.75F,
                                3.0F, 2.0F, 2.0F,
                                hornDeform
                        ),
                PartPose.ZERO
        );


        head.addOrReplaceChild(
                "damask_horn_right",
                CubeListBuilder.create()
                        .texOffs(54, 16)
                        .addBox(
                                8.9375F + hornXOffset,
                                -5.0F,
                                -4.75F,
                                3.0F, 2.0F, 2.0F,
                                hornDeform
                        ),
                PartPose.ZERO
        );
        return mesh;
    }

    public static LayerDefinition createLayerDefinition(CubeDeformation deformation) {
        return LayerDefinition.create(createBodyLayer(deformation), 64, 32);
    }
}
