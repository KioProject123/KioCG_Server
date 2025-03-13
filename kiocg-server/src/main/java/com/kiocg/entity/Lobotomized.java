package com.kiocg.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class Lobotomized {
    public boolean isLobotomized = false;

    public boolean checkLobotomized(LivingEntity livingEntity) {
        int interval = livingEntity.level().paperConfig().kiocgConfig.entity.animal.lobotomizeCheckInterval;
        if (interval <= 0) {
            return this.isLobotomized = false;
        }

        if ((livingEntity.getId() + livingEntity.tickCount) % interval == 0) {
            if (!livingEntity.onGround()) {
                this.isLobotomized = false;
            } else if (livingEntity.isPassenger()) {
                this.isLobotomized = true;
            } else {
                this.isLobotomized = !canTravel(livingEntity);
            }
        }
        return this.isLobotomized;
    }

    private boolean canTravel(LivingEntity livingEntity) {
        // offset Y for short blocks like dirt_path/farmland
        // 扩展到半砖高度 (希望不会出问题!)
        BlockPos pos = BlockPos.containing(livingEntity.getX(), livingEntity.getY() + 0.500001D, livingEntity.getZ());
        Level level = livingEntity.level();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (canTravelTo(mutablePos.setWithOffset(pos, direction), level)) {
                return true;
            }
        }
        return false;
    }

    private boolean canTravelTo(BlockPos.MutableBlockPos mutablePos, Level level) {
        BlockState state = level.getBlockStateIfLoaded(mutablePos);
        if (state == null) {
            // chunk not loaded
            return false;
        }

        // only if both blocks have no collision
        return isPathfindable(state) && isPathfindable(level.getBlockState(mutablePos.move(Direction.UP)));
    }

    private boolean isPathfindable(BlockState state) {
        return state.isAir() || state.isPathfindable(PathComputationType.LAND);
    }
}
