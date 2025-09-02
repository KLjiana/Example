package com.kaleblangley.summon_bench.common.block.entity;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.config.ConfigLoader;
import com.kaleblangley.summon_bench.common.init.BlockEntityInit;
import com.kaleblangley.summon_bench.common.menu.SummonMenu;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SummonBenchBlockEntity extends BaseContainerBlockEntity implements GeoBlockEntity {
    private static final RawAnimation STOP = RawAnimation.begin().thenPlay("stop");
    private static final RawAnimation CLOSE = RawAnimation.begin().thenPlay("close");
    private static final RawAnimation OPEN = RawAnimation.begin().thenPlay("open");

    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
    private ItemStack itemStack = ItemStack.EMPTY;

    public SummonBenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.SUMMON_BENCH.get(), pos, blockState);
    }

    public void summonEntity(ServerLevel serverLevel) {
        var entries = ConfigLoader.getConfig().getSummonEntity(itemStack);
        if (entries.isEmpty()) {
            SummonBench.LOGGER.warn("Can't match any entity by {} in Server Side", itemStack);
            return;
        }

        if (entries.get(0).summonEntity(serverLevel, this.worldPosition.above())) {
            itemStack.shrink(1);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controller) {
        controller.add(new AnimationController<>(this, state -> {
            SummonBenchBlockEntity benchBlock = state.getAnimatable();
            BlockPos blockPos = benchBlock.worldPosition;
            if (benchBlock.getLevel().hasNearbyAlivePlayer(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, 5)) {
                if (!itemStack.isEmpty()) {
                    return state.setAndContinue(CLOSE);
                }
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
        return new SummonMenu(containerId, inventory, this, this.worldPosition);
    }


    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == 0 && !itemStack.isEmpty()) {
            return itemStack.split(amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot == 0) {
            ItemStack old = itemStack;
            itemStack = ItemStack.EMPTY;
            return old;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        if (slot == 0) {
            itemStack = stack;
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        itemStack = ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, NonNullList.withSize(1, itemStack));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, list);
        itemStack = list.get(0);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        ContainerHelper.saveAllItems(updateTag, NonNullList.withSize(1, itemStack));
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, list);
        itemStack = list.get(0);
    }
}
