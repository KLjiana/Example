package vice.sol_valheim.attchment;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import vice.sol_valheim.network.SyncFoodDataPacket;

import java.util.List;

public class FoodDataAttachment implements IFoodSlots, INBTSerializable<CompoundTag> {
    private final List<FoodInstance> slots;

    public FoodDataAttachment(){
        slots = new ObjectArrayList<>();
    }

    public FoodDataAttachment(List<FoodInstance> slots){
        this.slots = slots;
    }

    @Override
    public List<FoodInstance> getSlots() {
        return slots;
    }

    @Override
    public boolean addFood(FoodData.FoodInfo info, Player player) {
        if (isFull()) return false;
        slots.add(new FoodInstance(info, info.getDurationTicks()));
        //TODO NETWORK, PLAYER HEARTH
        PacketDistributor.sendToAllPlayers(new SyncFoodDataPacket(serialize()));
        return true;
    }

    @Override
    public void tick() {
        slots.removeIf(inst -> {
            inst.decrement();
            return inst.getRemainingTicks() <= 0;
        });
    }

    @Override
    public void clear(Player player) {
        slots.clear();
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncFoodDataPacket(serialize()));
    }

    @Override
    public boolean isFull() {
        //TODO CONFIG
        return slots.size() >= 4;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return serialize();
    }

    public CompoundTag serialize(){
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (FoodInstance inst : slots) {
            CompoundTag instTag = new CompoundTag();
            instTag.putString("item", inst.getInfo().getId().toString());
            instTag.putInt("remaining", inst.getRemainingTicks());
            list.add(instTag);
        }
        tag.put("slots", list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        deserialize(nbt);
    }

    public void deserialize(CompoundTag nbt){
        slots.clear();
        ListTag list = nbt.getList("slots", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag instTag = list.getCompound(i);
            ResourceLocation id = ResourceLocation.parse(instTag.getString("item"));
            FoodData.FoodInfo info = FoodData.getInfo(id);
            if (info != null) {
                int rem = instTag.getInt("remaining");
                slots.add(new FoodInstance(info, rem));
            }
        }
    }
}
