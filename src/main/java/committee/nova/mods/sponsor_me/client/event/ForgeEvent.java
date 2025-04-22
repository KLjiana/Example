package committee.nova.mods.sponsor_me.client.event;

import committee.nova.mods.sponsor_me.SponsorMeMod;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SponsorMeMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEvent {
    @SubscribeEvent
    public static void addButton(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof JoinMultiplayerScreen screen){
            Button button = new Button.Builder(Component.literal("订阅我"),
                    ConfirmLinkScreen.confirmLink("https://aka.ms/MinecraftJavaLicenses", screen, true))
                    .size(300, 32)
                    .pos(screen.width / 2 - 300 / 2 + 2, 32)
                    .build();

            event.addListener(button);
        }
    }
}
