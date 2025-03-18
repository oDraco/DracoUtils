package com.github.oDraco.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class BlockUtils {

    public static Block[] getCubicSelection(Block center, int radius) {
        if (radius < 0)
            return new Block[]{};

        int iterations = (radius * 2) + 1;
        Block[] blocks = new Block[iterations * iterations * iterations];
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

    public static Stream<Location> getCubicSelection(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld())
            throw new IllegalArgumentException("Both locations need to be in the same world");
        double xMin = Math.min(loc1.getX(), loc2.getX());
        double xMax = Math.max(loc1.getX(), loc2.getX());
        double yMin = Math.min(loc1.getY(), loc2.getY());
        double yMax = Math.max(loc1.getY(), loc2.getY());
        double zMin = Math.min(loc1.getZ(), loc2.getZ());
        double zMax = Math.max(loc1.getZ(), loc2.getZ());
        Iterator<Location> iterator = new Iterator<Location>() {
            private double currentX = xMin;
            private double currentY = yMin;
            private double currentZ = zMin;
            private boolean done = false;

            @Override
            public boolean hasNext() {
                return !done;
            }

            @Override
            public Location next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Location nextLoc = new Location(loc1.getWorld(), currentX, currentY, currentZ);
                if (currentZ < zMax) {
                    currentZ++;
                } else if (currentY < yMax) {
                    currentZ = zMin;
                    currentY++;
                } else if (currentX < xMax) {
                    currentZ = zMin;
                    currentY = yMin;
                    currentX++;
                } else
                    done = true;
                return nextLoc;
            }
        };
        Iterable<Location> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static Location[] getLocationsBetween(Location loc1, Location loc2, int steps) {
        Location[] locs = new Location[steps];

        double diffX = loc1.getX() - loc2.getX();
        double diffY = loc1.getY() - loc2.getY();
        double diffZ = loc1.getZ() - loc2.getZ();

        double stepX = diffX / steps;
        double stepY = diffY / steps;
        double stepZ = diffZ / steps;

        for (int i = 0; i < steps; i++) {
            locs[i] = loc1.clone().subtract(stepX * i, stepY * i, stepZ * i);
        }

        return locs;
    }
}
