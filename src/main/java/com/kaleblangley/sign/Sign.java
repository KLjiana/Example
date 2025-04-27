package com.kaleblangley.sign;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;


@Mod(Sign.MODID)
public class Sign {
    public static final String MODID = "sign_show";
    public Sign(){
        MinecraftForge.EVENT_BUS.addListener(Sign::rightSign);
    }

    private static void rightSign(PlayerInteractEvent.RightClickBlock event){
        Level level = event.getLevel();
        if (!level.isClientSide) {
            Player player = event.getEntity();
            BlockEntity blockEntity = level.getBlockEntity(event.getHitVec().getBlockPos());

            if (player.isCrouching() && player.getMainHandItem().is(Items.AIR) && blockEntity instanceof SignBlockEntity signBlockEntity) {
                SignText signText = signBlockEntity.getTextFacingPlayer(player);
                Arrays.stream(signText.getMessages(false)).forEach(component -> sendMessage(component, signText, player));
                event.setCanceled(true);
            }
        }
    }

    protected static void sendMessage(Component component, SignText signText, Player player){
        int textColor = signText.getColor().getTextColor();
        int color = textColor == 0 ? 16777215 : textColor;
        player.sendSystemMessage(component.copy().withStyle(Style.EMPTY.withColor(color)));
    }
}
