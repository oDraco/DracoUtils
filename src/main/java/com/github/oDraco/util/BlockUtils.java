package com.github.oDraco.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

public abstract class BlockUtils {

    public static Block[] getCubicSelection(Block center, int radius) {
        if(radius < 0)
            return new Block[] {};

        int iterations = (radius * 2) + 1;
        Block[] blocks = new Block[iterations*iterations*iterations];
        int index = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks[index] = center.getRelative(x, y, z);
                    index++;
                }
            }
        }
        return blocks;
    }

    public static Location[] getLocationsBetween(Location loc1, Location loc2, int steps) {
        Location[] locs = new Location[steps];

        double diffX = loc1.getX()-loc2.getX();
        double diffY = loc1.getY()-loc2.getY();
        double diffZ = loc1.getZ()-loc2.getZ();

        double stepX = diffX/steps;
        double stepY = diffY/steps;
        double stepZ = diffZ/steps;

        for (int i = 0; i < steps; i++) {
            locs[i] = loc1.clone().subtract(stepX*i, stepY*i, stepZ*i);
        }

        return locs;
    }
}
