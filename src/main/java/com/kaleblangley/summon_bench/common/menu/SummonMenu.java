package com.kaleblangley.summon_bench.common.menu;

import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import com.kaleblangley.summon_bench.common.init.MenuInit;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

public class SummonMenu extends AbstractContainerMenu {
    public final Container summon;
    public final BlockPos entityPos;

    public SummonMenu(int containerId, Container container, FriendlyByteBuf extraData) {
        this(containerId, container, new SimpleContainer(1), extraData.readBlockPos());
    }

    public SummonMenu(int containerId, Container inventory, Container summonEntity, BlockPos blockPos) {
        super(MenuInit.SUMMON_MENU.get(), containerId);
        this.summon = summonEntity;
        this.entityPos = blockPos;
        Slot summonSlot = new Slot(this.summon, 0, 80, 44);
        this.addSlot(summonSlot);

        //Player slot
        int startX = 5;
        int startY = 123;
        int slotX = 20;
        int slotY = 20;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + k * 9 + 9, startX + l * slotX, startY + k * slotY));
            }
        }
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, startX + i1 * slotX, startY + (slotY * 3 + 6)));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }

                if (index >= 1 && index < 28) {
                    if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 28 && index < 37) {
                    if (!this.moveItemStackTo(itemstack1, 1, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}
