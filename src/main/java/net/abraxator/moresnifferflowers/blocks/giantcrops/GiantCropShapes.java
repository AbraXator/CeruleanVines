package net.abraxator.moresnifferflowers.blocks.giantcrops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GiantCropShapes {
    public static VoxelShape carrot(BlockPos posInCrop) {
        var straight1 = Block.box(5, 0, 16, 16, 16, 16);
        var corner1 = Block.box(5, 0, 5, 16, 16, 16);
        var straight2 = Block.box(9, 0, 16, 16, 15, 16);
        var corner2 = Block.box(9, 0, 9, 16, 15, 16);
        var dir = Direction.NORTH;
        
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if(posInCrop.equals(new BlockPos(0, -1, 0).relative(direction))) {
                return rotateShape(dir, direction, straight1);
            }
            
            BlockPos blockPos = new BlockPos(0, -1, 0).relative(direction.getClockWise()).relative(direction);
            if(posInCrop.equals(blockPos)) {
                return rotateShape(dir, direction, corner1);
            }
            
            dir = direction;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if(posInCrop.equals(new BlockPos(0, 0, 0).relative(direction))) {
                return rotateShape(dir, direction, straight2);
            }

            if(posInCrop.equals(new BlockPos(0, 0, 0).relative(direction.getClockWise()).relative(direction))) {
                return rotateShape(dir, direction, corner2);
            }

            dir = direction;
        }
        
        if(posInCrop.getX() == 0 && posInCrop.getZ() == 0 && posInCrop.getY() <= 1) {
            return Shapes.block();
        }
        
        return Shapes.block();
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.ordinal() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }
}
