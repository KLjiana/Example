package com.kaleblangley.wrench_plus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaleblangley.wrench_plus.WrenchPlus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WrenchConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_DIR = "config";
    private static final String CONFIG_FILE = "wrench_plus.json";
    
    private static WrenchConfigData configData;
    
    public static class WrenchConfigData {
        public float attackDamage = 6.0f;
        public float attackSpeed = -2.4f;
    }
    
    public static void load() {
        Path configPath = Paths.get(CONFIG_DIR, CONFIG_FILE);
        
        try {
            Files.createDirectories(configPath.getParent());
            
            if (Files.exists(configPath)) {
                try (FileReader reader = new FileReader(configPath.toFile())) {
                    configData = GSON.fromJson(reader, WrenchConfigData.class);
                    if (configData == null) {
                        configData = new WrenchConfigData();
                    }
                } catch (Exception e) {
                    WrenchPlus.LOGGER.error("Failed to load config, using defaults", e);
                    configData = new WrenchConfigData();
                }
            } else {
                configData = new WrenchConfigData();
                save();
            }
        } catch (IOException e) {
            WrenchPlus.LOGGER.error("Failed to create config directory", e);
            configData = new WrenchConfigData();
        }
    }
    
    public static void save() {
        if (configData == null) {
            configData = new WrenchConfigData();
        }
        
        Path configPath = Paths.get(CONFIG_DIR, CONFIG_FILE);
        
        try {
            Files.createDirectories(configPath.getParent());
            
            try (FileWriter writer = new FileWriter(configPath.toFile())) {
                GSON.toJson(configData, writer);
            }
        } catch (IOException e) {
            WrenchPlus.LOGGER.error("Failed to save config", e);
        }
    }
    
    public static float getAttackDamage() {
        if (configData == null) {
            load();
        }
        return configData.attackDamage;
    }
    
    public static float getAttackSpeed() {
        if (configData == null) {
            load();
        }
        return configData.attackSpeed;
    }
    
    public static void setAttackDamage(float damage) {
        if (configData == null) {
            load();
        }
        configData.attackDamage = damage;
        save();
    }
    
    public static void setAttackSpeed(float speed) {
        if (configData == null) {
            load();
        }
        configData.attackSpeed = speed;
        save();
    }
}