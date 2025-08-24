package com.kaleblangley.summon_bench.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaleblangley.summon_bench.SummonBench;
import net.minecraftforge.fml.loading.FMLLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ConfigLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SummonConfig config;
    
    public static void loadConfig() {
        Path configPath = FMLLoader.getGamePath().resolve("config").resolve("summon_config.json");
        
        try {
            Files.createDirectories(configPath.getParent());
            
            if (!Files.exists(configPath)) {
                SummonBench.LOGGER.warn("Could not find summon_config.json at {}, creating default configuration", configPath);
                config = createDefaultConfig();
                saveConfig(configPath);
                return;
            }
            
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(configPath), StandardCharsets.UTF_8)) {
                config = GSON.fromJson(reader, SummonConfig.class);
                if (config == null || config.getSummonList() == null) {
                    SummonBench.LOGGER.warn("Invalid summon_config.json format, using default configuration");
                    config = createDefaultConfig();
                }
                SummonBench.LOGGER.info("Successfully loaded summon configuration with {} entries from {}", 
                    config.getSummonList().size(), configPath);
            }
        } catch (IOException e) {
            SummonBench.LOGGER.error("Failed to load summon_config.json from {}", configPath, e);
            config = createDefaultConfig();
        } catch (Exception e) {
            SummonBench.LOGGER.error("Error parsing summon_config.json from {}", configPath, e);
            config = createDefaultConfig();
        }
    }
    
    private static SummonConfig createDefaultConfig() {
        SummonConfig defaultConfig = new SummonConfig();
        defaultConfig.setSummonList(new ArrayList<>());
        
        SummonConfig.SummonEntry exampleEntry = new SummonConfig.SummonEntry();
        
        SummonConfig.Entity entity = new SummonConfig.Entity();
        entity.setId("minecraft:zombie");
        entity.setNbt("");
        exampleEntry.setEntity(entity);
        
        SummonConfig.Item item = new SummonConfig.Item();
        item.setId("minecraft:rotten_flesh");
        item.setNbt("");
        exampleEntry.setItem(item);
        
        defaultConfig.getSummonList().add(exampleEntry);
        
        return defaultConfig;
    }
    
    public static SummonConfig getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }
    
    public static void reloadConfig() {
        loadConfig();
    }
    
    private static void saveConfig(Path configPath) {
        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(configPath), StandardCharsets.UTF_8)) {
            GSON.toJson(config, writer);
            SummonBench.LOGGER.info("Default configuration saved to {}", configPath);
        } catch (IOException e) {
            SummonBench.LOGGER.error("Failed to save default configuration to {}", configPath, e);
        }
    }
}