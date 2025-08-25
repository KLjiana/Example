package com.kaleblangley.summon_bench.common.config;

import com.kaleblangley.summon_bench.SummonBench;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SummonConfig {
    private List<SummonEntry> summon_list;

    public SummonConfig() {
    }

    public List<SummonEntry> getSummonList() {
        return summon_list;
    }

    public void setSummonList(List<SummonEntry> summon_list) {
        this.summon_list = summon_list;
    }

    public static class SummonEntry {
        private EntityRecord entityRecord;
        private ItemRecord itemRecord;

        public SummonEntry() {
        }

        public Entity summonEntity(ServerLevel level, BlockPos pos) throws CommandSyntaxException {
            return entityRecord.getEntity(level, pos);
        }

        public void setEntity(EntityRecord entityRecord) {
            this.entityRecord = entityRecord;
        }

        public ItemStack getItem() throws CommandSyntaxException {
            return itemRecord.getItem();
        }

        public void setItem(ItemRecord itemRecord) {
            this.itemRecord = itemRecord;
        }
    }

    public static CompoundTag parseTag(String nbt) throws CommandSyntaxException {
        return TagParser.parseTag(nbt).copy();
    }

    public static class EntityRecord {
        private String id;
        private String nbt;

        public EntityRecord() {
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

        public Entity getEntity(ServerLevel level, BlockPos pos) throws CommandSyntaxException {
            CompoundTag compoundTag = parseTag(nbt);
            compoundTag.putString("id", id);

            Entity entity = EntityType.loadEntityRecursive(compoundTag, level, funcEntity -> {
                funcEntity.moveTo(pos.getX(), pos.getY(), pos.getZ());
                return funcEntity;
            });
            if (entity == null) {
                SummonBench.LOGGER.warn("Can't find entity {} with tag {}", id, nbt);
            } else {
                level.tryAddFreshEntityWithPassengers(entity);
            }
            return entity;
        }
    }

    public static class ItemRecord {
        private String id;
        private int count = 1;
        private String nbt;

        public ItemRecord() {
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

        public ItemStack getItem() throws CommandSyntaxException {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            if (item == null) {
                SummonBench.LOGGER.warn("Can't find item {}x {} with {}", count, id, nbt);
                return ItemStack.EMPTY;
            } else {
                return new ItemStack(item, count, parseTag(nbt));
            }
        }
    }
}