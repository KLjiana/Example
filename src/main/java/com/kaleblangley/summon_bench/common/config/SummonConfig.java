package com.kaleblangley.summon_bench.common.config;

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
        private Entity entity;
        private Item item;

        public SummonEntry() {
        }

        public Entity getEntity() {
            return entity;
        }

        public void setEntity(Entity entity) {
            this.entity = entity;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }
    }

    public static class Entity {
        private String id;
        private String nbt;

        public Entity() {
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
    }

    public static class Item {
        private String id;
        private String nbt;

        public Item() {
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
    }
}