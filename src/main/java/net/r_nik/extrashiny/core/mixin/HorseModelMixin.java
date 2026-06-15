package net.r_nik.extrashiny.core.mixin;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.r_nik.extrashiny.item.DamaskHorseArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseModel.class)
public abstract class HorseModelMixin<T extends AbstractHorse> extends AgeableListModel<T> {

    @Shadow @Final
    protected ModelPart headParts;

    @Shadow @Final
    protected ModelPart body;

    @Inject(method = "createBodyMesh", at = @At("TAIL"))
    private static void extrashiny$createBodyMesh(CubeDeformation deformation,
                                                  CallbackInfoReturnable<MeshDefinition> cir) {

        MeshDefinition mesh = cir.getReturnValue();
        PartDefinition root = mesh.getRoot();
        PartDefinition headParts = root.getChild("head_parts");
        PartDefinition head = headParts.getChild("head");

        head.addOrReplaceChild(
                "extrashiny_damask_horn_left",
                CubeListBuilder.create()
                        .texOffs(56, 8)
                        .addBox(3.0F, -10.0F, 2.0F, 2.0F, 2.0F, 2.0F)
                        .texOffs(56, 0)
                        .addBox(5.0F, -14.0F, 2.0F, 2.0F, 6.0F, 2.0F),
                PartPose.ZERO
        );


        head.addOrReplaceChild(
                "extrashiny_damask_horn_right",
                CubeListBuilder.create()
                        .texOffs(56, 8)
                        .addBox(-5.0F, -10.0F, 2.0F, 2.0F, 2.0F, 2.0F)
                        .texOffs(56, 0)
                        .addBox(-7.0F, -14.0F, 2.0F, 2.0F, 6.0F, 2.0F),
                PartPose.ZERO
        );
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;FFFFF)V", at = @At("TAIL"))
    private void extrashiny$setupAnim(T entity,
                                      float limbSwing, float limbSwingTicks, float ageInTicks,
                                      float netHeadYaw, float headPitch,
                                      CallbackInfo ci) {

        ModelPart head = this.headParts.getChild("head");
        ModelPart hornLeft = head.getChild("extrashiny_damask_horn_left");
        ModelPart hornRight = head.getChild("extrashiny_damask_horn_right");

        boolean hasDamaskArmor =
                (entity instanceof Horse horse) &&
                        (horse.getArmor().getItem() instanceof DamaskHorseArmorItem);

        hornLeft.visible = hasDamaskArmor;
        hornRight.visible = hasDamaskArmor;
    }
}