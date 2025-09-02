package com.kaleblangley.summon_bench.network.c2s;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.block.entity.SummonBenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SummonEntityC2SPacket(BlockPos blockEntityPos) {
    public static void encode(SummonEntityC2SPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(packet.blockEntityPos);
    }

    public static SummonEntityC2SPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new SummonEntityC2SPacket(friendlyByteBuf.readBlockPos());
    }

    public static void handle(SummonEntityC2SPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer == null) {
                SummonBench.LOGGER.error("Can't summon entity because the serverPlayer is null");
                return;
            }
            ServerLevel serverLevel = serverPlayer.serverLevel();
            var blockEntity = serverLevel.getBlockEntity(packet.blockEntityPos);
            if (blockEntity instanceof SummonBenchBlockEntity benchEntity) {
                benchEntity.summonEntity(serverLevel);
            }
            SummonBench.LOGGER.error("Can't summon entity because the blockPos can't find summonBenchBlockEntity");
        });
        context.setPacketHandled(true);
    }
}
