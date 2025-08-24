package com.kaleblangley.summon_bench.common.block.entity;

import com.kaleblangley.summon_bench.common.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SummonBenchBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation STOP = RawAnimation.begin().thenPlay("stop");
    private static final RawAnimation CLOSE = RawAnimation.begin().thenPlay("close");
    private static final RawAnimation OPEN = RawAnimation.begin().thenPlay("open");

    public SummonBenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.SUMMON_BENCH.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controller) {
        controller.add(new AnimationController<>(this, state -> {
            state.getAnimatable().level.getNearestPlayer()
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
