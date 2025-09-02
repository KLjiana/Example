package com.kaleblangley.summon_bench.network.s2c;

import com.kaleblangley.summon_bench.common.config.ConfigLoader;
import com.kaleblangley.summon_bench.common.config.SummonConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ConfigSyncS2CPacket(SummonConfig entries) {
    public static void encode(ConfigSyncS2CPacket packet, FriendlyByteBuf byteBuf) {
        packet.entries.writeByte(byteBuf);
    }

    public static ConfigSyncS2CPacket decode(FriendlyByteBuf byteBuf) {
        return new ConfigSyncS2CPacket(new SummonConfig(byteBuf));
    }

    public static void handle(ConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ConfigLoader.resetList(packet.entries.getSummonList()
        ));
    }
}
