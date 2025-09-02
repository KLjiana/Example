package com.kaleblangley.summon_bench.common.event;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.config.ConfigLoader;
import com.kaleblangley.summon_bench.network.NetworkHandler;
import com.kaleblangley.summon_bench.network.s2c.ConfigSyncS2CPacket;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SummonBench.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal(SummonBench.MODID)
                        .requires(stack -> stack.hasPermission(2))
                        .then(
                                Commands.literal("reload_config")
                                        .executes(commandContext -> {
                                            ConfigLoader.reloadConfig();
                                            NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new ConfigSyncS2CPacket(ConfigLoader.getConfig()));
                                            return 0;
                                        })
                        )
        );
    }
}
