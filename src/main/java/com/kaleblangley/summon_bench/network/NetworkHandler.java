package com.kaleblangley.summon_bench.network;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.network.c2s.SummonEntityC2SPacket;
import com.kaleblangley.summon_bench.network.s2c.ConfigSyncS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SummonBench.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;
    public static void register() {
        CHANNEL.registerMessage(id++, SummonEntityC2SPacket.class, SummonEntityC2SPacket::encode, SummonEntityC2SPacket::decode, SummonEntityC2SPacket::handle);
        CHANNEL.registerMessage(id++, ConfigSyncS2CPacket.class, ConfigSyncS2CPacket::encode, ConfigSyncS2CPacket::decode, ConfigSyncS2CPacket::handle);
    }
}
