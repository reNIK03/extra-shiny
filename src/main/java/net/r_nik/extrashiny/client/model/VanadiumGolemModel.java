package net.r_nik.extrashiny.client.model;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.r_nik.extrashiny.entity.animations.ModAnimationDefinitions.VanadiumGolemAnimation;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity;
import net.r_nik.extrashiny.entity.VanadiumGolemEntity.AttackType;
import net.r_nik.extrashiny.entity.animations.ModAnimationDefinitions.VanadiumGolemAnimation;



public class VanadiumGolemModel<T extends Entity> extends HierarchicalModel<T> {


    public void applyHeadTransform(PoseStack poseStack) {
        this.Main.translateAndRotate(poseStack);
        this.LowerBody.translateAndRotate(poseStack);
        this.UpperBody.translateAndRotate(poseStack);
        this.Head.translateAndRotate(poseStack);
    }


    public ModelPart getHead() {
        return this.Head;
    }


    private final ModelPart Main;
    private final ModelPart Hip;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart LowerBody;
    private final ModelPart UpperBody;
    private final ModelPart Head;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final ModelPart Cloth;
    private final ModelPart Back;
    private final ModelPart Front;

    public VanadiumGolemModel(ModelPart root) {
        this.Main = root.getChild("Main");
        this.Hip = this.Main.getChild("Hip");
        this.RightLeg = this.Hip.getChild("RightLeg");
        this.LeftLeg = this.Hip.getChild("LeftLeg");
        this.LowerBody = this.Main.getChild("LowerBody");
        this.UpperBody = this.LowerBody.getChild("UpperBody");
        this.Head = this.UpperBody.getChild("Head");
        this.LeftArm = this.UpperBody.getChild("LeftArm");
        this.RightArm = this.UpperBody.getChild("RightArm");
        this.Cloth = this.UpperBody.getChild("Cloth");
        this.Back = this.Cloth.getChild("Back");
        this.Front = this.Cloth.getChild("Front");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, -3.0F));

        PartDefinition Hip = Main.addOrReplaceChild("Hip", CubeListBuilder.create().texOffs(72, 24).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -20.0F, 3.0F));

        PartDefinition RightLeg = Hip.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(80, 87).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 25.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 3.0F, 0.0F));

        PartDefinition LeftLeg = Hip.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(104, 87).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 25.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 3.0F, 0.0F));

        PartDefinition LowerBody = Main.addOrReplaceChild("LowerBody", CubeListBuilder.create().texOffs(0, 30).addBox(-9.0F, -12.0F, -9.0F, 18.0F, 12.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.0F, 3.0F));

        PartDefinition UpperBody = LowerBody.addOrReplaceChild("UpperBody", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -3.0F, -9.0F, 18.0F, 12.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -21.0F, 0.0F));

        PartDefinition Head = UpperBody.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(116, 41).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition LeftArm = UpperBody.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 60).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 50.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 1.0F, 0.0F));

        PartDefinition RightArm = UpperBody.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 60).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 50.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, 1.0F, 0.0F));

        PartDefinition Cloth = UpperBody.addOrReplaceChild("Cloth", CubeListBuilder.create().texOffs(72, 0).addBox(-9.0F, -3.0F, -9.0F, 18.0F, 6.0F, 18.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Back = Cloth.addOrReplaceChild("Back", CubeListBuilder.create().texOffs(80, 41).addBox(-9.0F, 0.0F, 0.0F, 18.0F, 23.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 3.0F, 9.0F));

        PartDefinition Front = Cloth.addOrReplaceChild("Front", CubeListBuilder.create().texOffs(80, 64).addBox(-9.0F, 0.0F, 0.0F, 18.0F, 23.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 3.0F, -9.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(
            Entity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {

        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (!(entity instanceof VanadiumGolemEntity golem)) return;

        this.animate(golem.idleAnimationState, VanadiumGolemAnimation.Idle, ageInTicks);
        this.animate(golem.walkAnimationState, VanadiumGolemAnimation.Walk, ageInTicks);

        if (golem.isAttacking()) {
            switch (golem.getAttackType()) {
                case SMALL ->
                        this.animate(golem.attackAnimationState,
                                VanadiumGolemAnimation.SmallHit,
                                ageInTicks);
                case MEDIUM ->
                        this.animate(golem.attackAnimationState,
                                VanadiumGolemAnimation.MediumHit,
                                ageInTicks);
                case HEAVY ->
                        this.animate(golem.attackAnimationState,
                                VanadiumGolemAnimation.HeavyHit,
                                ageInTicks);
            }
        }

        this.Head.yRot += netHeadYaw * ((float)Math.PI / 180F);
        this.Head.xRot += headPitch * ((float)Math.PI / 180F);

    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.Main;
    }
}