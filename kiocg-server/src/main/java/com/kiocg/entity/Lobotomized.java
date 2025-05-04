package com.kiocg.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.Path;

import java.util.LinkedHashSet;
import java.util.List;

public class Lobotomized {
    public boolean isLobotomized = false;

    public boolean checkLobotomized(PathfinderMob mob) {
        int interval = mob.level().paperConfig().kiocgConfig.entity.animal.lobotomizeCheckInterval;
        if (interval <= 0) {
            return this.isLobotomized = false;
        }

        if ((mob.getId() + mob.tickCount) % interval == 0) {
            this.isLobotomized = mob.isPassenger() || mob.onGround() && !canTravel(mob);
        }
        return this.isLobotomized;
    }

    private boolean canTravel(PathfinderMob mob) {
        int length = Direction.Plane.HORIZONTAL.length();
        BlockPos[] list = new BlockPos[length * 3];
        int i = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int index = i++;
            BlockPos relative = mob.blockPosition().relative(direction);
            list[index] = relative;
            list[index = index + length] = relative.above();
            list[index = index + length] = relative.below();
        }

        Path path = mob.getNavigation().createPath(new LinkedHashSet<>(List.of(list)), null, 0, false, 0, 2);
        return path != null && path.canReach();
    }
}
