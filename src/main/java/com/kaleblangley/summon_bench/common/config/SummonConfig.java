package com.kaleblangley.summon_bench.common.config;

import com.google.gson.annotations.SerializedName;
import com.kaleblangley.summon_bench.SummonBench;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class SummonConfig {
    private int block_unbroken_distance = 20;
    private List<SummonEntry> summon_list = new ArrayList<>();

    public SummonConfig() {
    }

    public SummonConfig(FriendlyByteBuf byteBuf) {
        this.block_unbroken_distance = byteBuf.readInt();
        this.summon_list = byteBuf.readList(SummonEntry::new);
    }

    public void writeByte(FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(block_unbroken_distance);
        byteBuf.writeCollection(summon_list, (byteBuf1, summonEntry) -> summonEntry.writeByte(byteBuf1));
    }

    public List<SummonEntry> getSummonList() {
        return summon_list;
    }

    public void setSummonList(List<SummonEntry> summon_list) {
        this.summon_list = summon_list;
    }

    public int getBlockUnbrokenDistance() {
        return block_unbroken_distance;
    }

    public void setBlockUnbrokenDistance(int block_unbroken_distance) {
        this.block_unbroken_distance = block_unbroken_distance;
    }

    public List<SummonEntry> getSummonEntity(ItemStack itemStack) {
        return this.summon_list.stream().filter(summonEntry -> {
            ItemStack entryItem = summonEntry.getItem();
            return ItemStack.isSameItemSameTags(itemStack, entryItem);
        }).toList();
    }

    public static class SummonEntry {
        @SerializedName(value = "entity", alternate = {"entityType"})
        private EntityRecord entityRecord;
        @SerializedName(value = "item", alternate = {"itemStack"})
        private ItemRecord itemRecord;

        public SummonEntry() {
        }

        public SummonEntry(FriendlyByteBuf byteBuf) {
            this.entityRecord = new EntityRecord(byteBuf);
            this.itemRecord = new ItemRecord(byteBuf);
        }

        public void writeByte(FriendlyByteBuf byteBuf) {
            entityRecord.writeByte(byteBuf);
            itemRecord.writeByte(byteBuf);
        }

        public boolean summonEntity(ServerLevel level, BlockPos pos) {
            return entityRecord.summonEntity(level, pos);
        }

        public void setEntity(EntityRecord entityRecord) {
            this.entityRecord = entityRecord;
        }

        public ItemStack getItem() {
            return itemRecord.getItemStack();
        }

        public void setItem(ItemRecord itemRecord) {
            this.itemRecord = itemRecord;
        }

        @Override
        public String toString() {
            return "SummonEntry{" +
                    "entityRecord=" + entityRecord.toString() +
                    ", itemRecord=" + itemRecord.toString() +
                    '}';
        }
    }

    public static CompoundTag parseTag(String nbt) {
        try {
            return TagParser.parseTag(nbt).copy();
        } catch (CommandSyntaxException e) {
            SummonBench.LOGGER.error("Can't parse {} nbt", nbt);
        }
        return new CompoundTag();
    }

    public static class EntityRecord {
        private String id;
        private String nbt = "{}";

        public EntityRecord() {
        }

        public EntityRecord(FriendlyByteBuf byteBuf) {
            this.id = byteBuf.readUtf();
            this.nbt = byteBuf.readUtf();
        }

        public void writeByte(FriendlyByteBuf byteBuf) {
            byteBuf.writeUtf(id);
            byteBuf.writeUtf(nbt);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNbt() {
            return nbt;
        }

        public void setNbt(String nbt) {
            this.nbt = nbt;
        }

        public boolean summonEntity(ServerLevel level, BlockPos pos) {
            CompoundTag compoundTag = parseTag(nbt);
            compoundTag.putString("id", id);

            Entity entity = EntityType.loadEntityRecursive(compoundTag, level, funcEntity -> {
                funcEntity.moveTo(pos.getX(), pos.getY(), pos.getZ());
                return funcEntity;
            });
            if (entity == null) {
                SummonBench.LOGGER.warn("Can't find entity {} with tag {}", id, nbt);
            } else {
                return level.tryAddFreshEntityWithPassengers(entity);
            }
            return false;
        }

        @Override
        public String toString() {
            return "EntityRecord{" +
                    "id='" + id + '\'' +
                    ", nbt='" + nbt + '\'' +
                    '}';
        }
    }

    public static class ItemRecord {
        private String id;
        private int count = 1;
        private String nbt = "{}";

        public ItemRecord() {
        }

        public ItemRecord(FriendlyByteBuf byteBuf) {
            this.id = byteBuf.readUtf();
            this.count = byteBuf.readInt();
            this.nbt = byteBuf.readUtf();
        }

        public void writeByte(FriendlyByteBuf byteBuf) {
            byteBuf.writeUtf(id);
            byteBuf.writeInt(count);
            byteBuf.writeUtf(nbt);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNbt() {
            return nbt;
        }

        public void setNbt(String nbt) {
            this.nbt = nbt;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public ItemStack getItemStack() {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            if (item == null) {
                SummonBench.LOGGER.warn("Can't find item {}x {} with {}", count, id, nbt);
                return ItemStack.EMPTY;
            } else {
                CompoundTag compoundTag = parseTag(nbt);
                return new ItemStack(item, count, compoundTag);
            }
        }

        @Override
        public String toString() {
            return "ItemRecord{" +
                    "id='" + id + '\'' +
                    ", count=" + count +
                    ", nbt='" + nbt + '\'' +
                    '}';
        }
    }
}