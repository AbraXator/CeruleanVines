package net.abraxator.moresnifferflowers.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import java.util.ArrayList;
import java.util.List;

public class CrookedCircle {
    public static List<BlockPos> crookedSphere(BlockPos center, int radius, RandomSource random) {
        List<BlockPos> ret = new ArrayList<>();
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    int distanceSquared = x * x + y * y + z * z;
                    SimplexNoise simplexNoise = new SimplexNoise(random);
                    double noiseValue = simplexNoise.getValue(x, y, z);
                    double distortedRadius = radius + noiseValue;
                    BlockPos pos = center.offset(x, y, z);

                    if (distanceSquared <= distortedRadius * distortedRadius) {
                        ret.add(pos);
                    }
                }
            }
        }
        
        return ret;
    }
}
