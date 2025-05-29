package vice.sol_valheim.event.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import vice.sol_valheim.SOLValheim;

@EventBusSubscriber(modid = SOLValheim.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameEvent {
    @SubscribeEvent
    public static void overlayEvent(RenderGuiEvent.Post event){

    }
}