package com.yungnickyoung.minecraft.yungsapi.world.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class BoundingBoxHelper {
    /**
     * Generates and rotates a block box.
     * The main axis is the primary of the x and z axes, in the direction
     * the structure should generate from the starting point.
     */
    public static BoundingBox boxFromCoordsWithRotation(int x, int y, int z, int secondaryAxisLen, int yLen, int mainAxisLen, Direction mainAxis) {
        BoundingBox boundingBox = new BoundingBox(x, y, z, x, y + yLen - 1, z);
        switch (mainAxis) {
            default -> {
                ((BoundingBox) boundingBox).setMaxX(x + (secondaryAxisLen - 1));
                ((BoundingBox) boundingBox).setMinZ(z - (mainAxisLen - 1));
            }
            case SOUTH -> {
                ((BoundingBox) boundingBox).setMinX(x - (secondaryAxisLen - 1));
                ((BoundingBox) boundingBox).setMaxZ(z + (mainAxisLen - 1));
            }
            case WEST -> {
                ((BoundingBox) boundingBox).setMinX(x - (mainAxisLen - 1));
                ((BoundingBox) boundingBox).setMinZ(z - (secondaryAxisLen - 1));
            }
            case EAST -> {
                ((BoundingBox) boundingBox).setMaxX(x + (mainAxisLen - 1));
                ((BoundingBox) boundingBox).setMaxZ(z + (secondaryAxisLen - 1));
            }
        }
        return boundingBox;
    }
}
