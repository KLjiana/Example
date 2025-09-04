package com.kaleblangley.summon_bench.common.event;

import com.kaleblangley.summon_bench.SummonBench;
import com.kaleblangley.summon_bench.common.config.ConfigLoader;
import com.kaleblangley.summon_bench.common.init.BlockInit;
import com.kaleblangley.summon_bench.network.NetworkHandler;
import com.kaleblangley.summon_bench.network.s2c.ConfigSyncS2CPacket;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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

    @SubscribeEvent
    public static void entityBrokenBlock(LivingDestroyBlockEvent event) {
        if (checkBlock(event.getState(), event.getPos(), event.getEntity().level())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void playerBrokenBlock(BlockEvent.BreakEvent event) {
        if (checkBlock(event.getState(), event.getPos(), event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entityPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (checkBlock(event.getPlacedBlock(), event.getPos(), event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void fluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        if (checkBlock(null, event.getPos(), event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void farmlandPlaceBlock(BlockEvent.FarmlandTrampleEvent event) {
        if (checkBlock(null, event.getPos(), event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void portalPlaceBlock(BlockEvent.PortalSpawnEvent event) {
        if (checkBlock(null, event.getPos(), event.getLevel())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void toolPlaceBlock(BlockEvent.BlockToolModificationEvent event) {
        if (checkBlock(null, event.getPos(), event.getLevel())) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void tntEvent(ExplosionEvent.Start event) {
        if (checkBlock(null, BlockPos.containing(event.getExplosion().getPosition()), event.getLevel())) {
            event.setCanceled(true);
        }
    }

    private static boolean checkBlock(@Nullable BlockState placeBlock, BlockPos blockPos, BlockGetter getter) {
        if (placeBlock != null && placeBlock.is(BlockInit.SUMMON_BENCH.get())) {
            return false;
        }
        int unbrokenDistance = ConfigLoader.getConfig().getBlockUnbrokenDistance();
        AABB aabb = new AABB(blockPos.offset(unbrokenDistance, unbrokenDistance, unbrokenDistance), blockPos.offset(-unbrokenDistance, -unbrokenDistance, -unbrokenDistance));
        Optional<BlockState> hasBench = getter.getBlockStates(aabb).filter(blockState -> blockState.is(BlockInit.SUMMON_BENCH.get())).findFirst();
        return hasBench.isPresent();
    }
}
