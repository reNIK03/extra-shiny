package net.r_nik.extrashiny.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.r_nik.extrashiny.entity.EnforcerEntity;
import net.r_nik.extrashiny.entity.animations.ModAnimationDefinitions;

public class EnforcerModel<T extends EnforcerEntity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart Main;
    private final ModelPart Thorax;
    private final ModelPart Head;
    private final ModelPart UpperJaw;
    private final ModelPart LowerJaw;
    private final ModelPart RightFrontLegs;
    private final ModelPart LeftFrontLegs;
    private final ModelPart Abdomen;
    private final ModelPart RightBackLegs;
    private final ModelPart LeftBackLegs;
    private final ModelPart Tail;
    private final ModelPart SculkSensor1;
    private final ModelPart SculkSensor2;

    public EnforcerModel(ModelPart root) {
        this.root = root;
        this.Main = root.getChild("Main");
        this.Thorax = this.Main.getChild("Thorax");
        this.Head = this.Thorax.getChild("Head");
        this.UpperJaw = this.Head.getChild("UpperJaw");
        this.LowerJaw = this.Head.getChild("LowerJaw");
        this.RightFrontLegs = this.Thorax.getChild("RightFrontLegs");
        this.LeftFrontLegs = this.Thorax.getChild("LeftFrontLegs");
        this.Abdomen = this.Main.getChild("Abdomen");
        this.RightBackLegs = this.Abdomen.getChild("RightBackLegs");
        this.LeftBackLegs = this.Abdomen.getChild("LeftBackLegs");
        this.Tail = this.Abdomen.getChild("Tail");
        this.SculkSensor1 = this.Tail.getChild("SculkSensor1");
        this.SculkSensor2 = this.Tail.getChild("SculkSensor2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild(
                "Main",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 8.0F, -3.0F)
        );

        PartDefinition thorax = main.addOrReplaceChild(
                "Thorax",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-6.0F, -5.0F, -6.0F, 12.0F, 11.0F, 12.0F, new CubeDeformation(0.01F))
                        .texOffs(36, 42).addBox(0.0F, -8.0F, -5.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(36, 47).addBox(0.0F, -7.0F, -2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 20).addBox(0.0F, -6.0F, 1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 1.0F)
        );

        PartDefinition head = thorax.addOrReplaceChild(
                "Head",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 2.0F, -6.0F)
        );

        head.addOrReplaceChild(
                "UpperJaw",
                CubeListBuilder.create()
                        .texOffs(0, 42).addBox(-4.0F, -4.0F, -9.0F, 8.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 0).addBox(-3.0F, -1.0F, -8.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(36, 51).addBox(-4.0F, 0.0F, -9.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.01F)),
                PartPose.offset(0.0F, 1.0F, 0.0F)
        );

        head.addOrReplaceChild(
                "LowerJaw",
                CubeListBuilder.create()
                        .texOffs(48, 10).addBox(-3.0F, -2.0F, -8.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.05F))
                        .texOffs(42, 23).addBox(-4.0F, 0.0F, -9.0F, 8.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 1.0F, 0.0F)
        );

        thorax.addOrReplaceChild(
                "RightFrontLegs",
                CubeListBuilder.create()
                        .texOffs(52, 60).addBox(-2.0F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 1.0F, -0.5F)
        );

        thorax.addOrReplaceChild(
                "LeftFrontLegs",
                CubeListBuilder.create()
                        .texOffs(0, 64).addBox(-1.0F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 1.0F, -0.5F)
        );

        PartDefinition abdomen = main.addOrReplaceChild(
                "Abdomen",
                CubeListBuilder.create()
                        .texOffs(0, 23).addBox(-5.0F, -2.0F, -1.0F, 10.0F, 8.0F, 11.0F, new CubeDeformation(0.01F)),
                PartPose.offset(0.0F, -2.0F, 5.0F)
        );

        abdomen.addOrReplaceChild(
                "RightBackLegs",
                CubeListBuilder.create()
                        .texOffs(12, 64).addBox(-1.0F, -2.0F, -1.5F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(3.0F, 6.0F, 6.5F)
        );

        abdomen.addOrReplaceChild(
                "LeftBackLegs",
                CubeListBuilder.create()
                        .texOffs(64, 51).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-3.0F, 6.0F, 6.5F)
        );

        PartDefinition tail = abdomen.addOrReplaceChild(
                "Tail",
                CubeListBuilder.create()
                        .texOffs(42, 35).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 68).addBox(-2.0F, -2.0F, 12.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 1.0F, 10.0F, -0.9599F, 0.0F, 0.0F)
        );

        tail.addOrReplaceChild(
                "SculkSensor1",
                CubeListBuilder.create()
                        .texOffs(0, 56).addBox(-5.0F, 0.0F, -2.0F, 5.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 0.0F, 14.0F)
        );

        tail.addOrReplaceChild(
                "SculkSensor2",
                CubeListBuilder.create()
                        .texOffs(26, 60).addBox(0.0F, 0.0F, -2.0F, 5.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 0.0F, 14.0F)
        );

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {

        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (entity.isHowling()) {
            this.animate(entity.howlAnimationState, ModAnimationDefinitions.EnforcerAnimation.Howl, ageInTicks);
            return;
        }

        this.animateWalk(ModAnimationDefinitions.EnforcerAnimation.Walk, limbSwing, limbSwingAmount, 6.0F, 1.0F);

        this.animate(entity.idleAnimationState, ModAnimationDefinitions.EnforcerAnimation.Idle, ageInTicks);

        this.animate(entity.sniffAnimationState, ModAnimationDefinitions.EnforcerAnimation.Sniff, ageInTicks);
        this.animate(entity.biteAnimationState, ModAnimationDefinitions.EnforcerAnimation.Bite, ageInTicks);

        int ticks = entity.getSignalTicks();
        if (ticks > 0) {
            float f = ticks / 18.0F;
            float wiggle = Mth.sin(ageInTicks * 1.2F) * 0.55F * f;

            this.SculkSensor1.zRot = wiggle;
            this.SculkSensor2.zRot = -wiggle;

            this.SculkSensor1.xRot = 0.15F * f;
            this.SculkSensor2.xRot = 0.15F * f;
        }

        boolean aggressive = entity.isAggressiveSynced();

        float defaultX = -0.9599F;
        float defaultY = 0.0F;
        float defaultZ = 0.0F;

        if (aggressive) {
            float upX = 1.92F;

            this.Tail.xRot = Mth.approach(this.Tail.xRot, upX, 1.80F);
            this.Tail.yRot = Mth.approach(this.Tail.yRot, 0.0F, 0.20F);
            this.Tail.zRot = Mth.approach(this.Tail.zRot, 0.0F, 0.20F);
        } else {
            this.Tail.xRot = Mth.approach(this.Tail.xRot, defaultX, 1.80F);
            this.Tail.yRot = Mth.approach(this.Tail.yRot, defaultY, 0.20F);
            this.Tail.zRot = Mth.approach(this.Tail.zRot, defaultZ, 0.20F);
        }


    }



    private void applyHeadLook(float netHeadYaw, float headPitch) {
        float yaw = netHeadYaw * (Mth.PI / 180F);
        float pitch = headPitch * (Mth.PI / 180F);

        yaw = Mth.clamp(yaw, -0.8F, 0.8F);
        pitch = Mth.clamp(pitch, -0.6F, 0.6F);

        this.Head.yRot += yaw;
        this.Head.xRot += pitch;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay,
                               int packedColor) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
    }
}
