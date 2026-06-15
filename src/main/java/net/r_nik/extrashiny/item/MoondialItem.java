package net.r_nik.extrashiny.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MoondialItem extends Item {

    public MoondialItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            int state = getMoondialState(level); // 0-8

            Component msg = switch (state) {
                case 0 -> Component.translatable("item.extrashiny.moondial.phase.full_moon");
                case 1 -> Component.translatable("item.extrashiny.moondial.phase.waning_gibbous");
                case 2 -> Component.translatable("item.extrashiny.moondial.phase.third_quarter");
                case 3 -> Component.translatable("item.extrashiny.moondial.phase.waning_crescent");
                case 4 -> Component.translatable("item.extrashiny.moondial.phase.new_moon");
                case 5 -> Component.translatable("item.extrashiny.moondial.phase.waxing_crescent");
                case 6 -> Component.translatable("item.extrashiny.moondial.phase.first_quarter");
                case 7 -> Component.translatable("item.extrashiny.moondial.phase.waxing_gibbous");
                default -> Component.translatable("item.extrashiny.moondial.phase.not_found");
            };

            player.displayClientMessage(msg.copy().withStyle(ChatFormatting.WHITE), true);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    public static int getMoondialState(Level level) {
        if (!level.dimension().equals(Level.OVERWORLD)) {
            return 8;
        }
        return level.getMoonPhase();
    }
}
