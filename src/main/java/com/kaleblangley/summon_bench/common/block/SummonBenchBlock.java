package com.kaleblangley.summon_bench.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.registry.BlockEntityRegistry;

public class SummonBenchBlock extends BaseEntityBlock implements EntityBlock {
    public SummonBenchBlock() {
        super(
                Properties.of()
                        .strength(-1.0F)
                        .noLootTable()
                        .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityRegistry.FERTILIZER_BLOCK.get().create(pos, state);
    }


    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
