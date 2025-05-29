package vice.sol_valheim.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import vice.sol_valheim.SOLValheim;
import vice.sol_valheim.network.SyncFoodDataPacket;

@EventBusSubscriber(modid = SOLValheim.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvent {
    @SubscribeEvent
    public static void registerNetWork(RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SyncFoodDataPacket.TYPE, SyncFoodDataPacket.STREAM_CODEC, SyncFoodDataPacket::handle);
    }
}
