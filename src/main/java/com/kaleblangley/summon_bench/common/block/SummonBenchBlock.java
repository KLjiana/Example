package com.kaleblangley.summon_bench.common.block;

import net.minecraft.world.level.block.Block;

public class SummonBenchBlock extends Block {
    public SummonBenchBlock() {
        super(
                Properties.of()
                        .strength(-1.0F)
                        .noLootTable()
                        .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
        );
    }
}
