package com.kaleblangley.summon_bench.common.block.entity;

import com.kaleblangley.summon_bench.common.init.BlockEntityInit;
import com.kaleblangley.summon_bench.common.menu.SummonMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SummonBenchBlockEntity extends BaseContainerBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation STOP = RawAnimation.begin().thenPlay("stop");
    private static final RawAnimation CLOSE = RawAnimation.begin().thenPlay("close");
    private static final RawAnimation OPEN = RawAnimation.begin().thenPlay("open");

    public SummonBenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.SUMMON_BENCH.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controller) {
        controller.add(new AnimationController<>(this, state -> {
            SummonBenchBlockEntity benchBlock = state.getAnimatable();
            BlockPos blockPos = benchBlock.worldPosition;
            if (benchBlock.getLevel().hasNearbyAlivePlayer(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, 5)) {
                return state.setAndContinue(OPEN);
            } else {
                return state.setAndContinue(CLOSE);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }   

    @Override
    protected Component getDefaultName() {
        return Component.literal("Summon Bench");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory) {
        return new SummonMenu(containerId, inventory);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int slot) {
        return null;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return null;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {

    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
    }
}
