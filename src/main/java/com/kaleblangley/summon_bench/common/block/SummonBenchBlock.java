package com.kaleblangley.summon_bench.common.block;

import com.kaleblangley.summon_bench.common.init.BlockEntityInit;
import com.kaleblangley.summon_bench.common.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.registry.BlockEntityRegistry;

public class SummonBenchBlock extends BaseEntityBlock implements EntityBlock {
    public SummonBenchBlock() {
        super(
                Properties.of()
                        .strength(-1.0F)
                        .noLootTable()
                        .noOcclusion()
                        .lightLevel(v -> 1)
                        .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
        );
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.SUMMON_BENCH.get().create(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider menuProvider = this.getMenuProvider(state, level, pos);
            if (menuProvider != null) {
                NetworkHooks.openScreen((ServerPlayer) player, menuProvider, pos);
            }
            return InteractionResult.CONSUME;
        }
    }
}
