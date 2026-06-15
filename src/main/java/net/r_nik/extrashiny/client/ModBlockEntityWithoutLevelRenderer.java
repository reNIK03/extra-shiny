package net.r_nik.extrashiny.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.r_nik.extrashiny.ExtraShiny;
import net.r_nik.extrashiny.client.model.BulwarkModel;
import net.r_nik.extrashiny.client.model.VanadiumPartisanModel;
import net.r_nik.extrashiny.item.ModItems;

public class ModBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public static final ModBlockEntityWithoutLevelRenderer INSTANCE = new ModBlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private final BlockEntityRenderDispatcher dispatcher;
    private final EntityModelSet modelSet;

    private VanadiumPartisanModel vanadiumPartisanModel;
    private BulwarkModel bulwarkModel;

    private static final ResourceLocation PARTISAN_MODEL_TEXTURE = ExtraShiny.res("textures/models/vanadium_partisan.png");
    private static final ResourceLocation BULWARK_MODEL_TEXTURE = ExtraShiny.res("textures/models/bulwark.png");

    public ModBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
        this.dispatcher = pBlockEntityRenderDispatcher;
        this.modelSet = pEntityModelSet;
    }

    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        this.vanadiumPartisanModel = new VanadiumPartisanModel(this.modelSet.bakeLayer(VanadiumPartisanModel.LAYER_LOCATION));
        this.bulwarkModel = new BulwarkModel(this.modelSet.bakeLayer(BulwarkModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.is(ModItems.VANADIUM_PARTISAN.get())) {
            pPoseStack.pushPose();
            pPoseStack.scale(1, -1, -1);
            VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.vanadiumPartisanModel.renderType(PARTISAN_MODEL_TEXTURE), false, pStack.hasFoil());
            this.vanadiumPartisanModel.renderToBuffer(pPoseStack, consumer, pPackedLight, pPackedOverlay, 1, 1, 1, 1);
            pPoseStack.popPose();
        }

        else if (pStack.is(ModItems.BULWARK.get())) {
            pPoseStack.pushPose();
            pPoseStack.scale(1, -1, -1);
            VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.bulwarkModel.renderType(BULWARK_MODEL_TEXTURE), false, pStack.hasFoil());
            this.bulwarkModel.renderToBuffer(pPoseStack, consumer, pPackedLight, pPackedOverlay, 1, 1, 1, 1);
            pPoseStack.popPose();
        }

        else {
            super.renderByItem(pStack, pDisplayContext, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }
    }
}