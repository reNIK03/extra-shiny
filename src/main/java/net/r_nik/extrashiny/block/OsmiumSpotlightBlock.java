package net.r_nik.extrashiny.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OsmiumSpotlightBlock extends FaceAttachedHorizontalDirectionalBlock {
    private static final VoxelShape SHAPE_FLOOR = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
    private static final VoxelShape SHAPE_CEILING = Block.box(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape SHAPE_WALL_NORTH = Block.box(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D);
    private static final VoxelShape SHAPE_WALL_SOUTH = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D);
    private static final VoxelShape SHAPE_WALL_WEST = Block.box(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
    private static final VoxelShape SHAPE_WALL_EAST = Block.box(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D);

    public OsmiumSpotlightBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACE, AttachFace.FLOOR)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACE)) {
            case FLOOR:
                return SHAPE_FLOOR;
            case CEILING:
                return SHAPE_CEILING;
            case WALL:
            default:
                return switch (state.getValue(FACING)) {
                    case NORTH -> SHAPE_WALL_NORTH;
                    case SOUTH -> SHAPE_WALL_SOUTH;
                    case WEST -> SHAPE_WALL_WEST;
                    case EAST -> SHAPE_WALL_EAST;
                    default -> SHAPE_WALL_NORTH;
                };
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE, FACING);
    }
}