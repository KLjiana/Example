package committee.nova.mods.sponsor_me.mixin;

import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractSelectionList.class)
public class AbstractSelectionListMixin {
    @Shadow protected int y1;

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 0), index = 2)
    public int noDown(int value){
        if ((Object) this instanceof ServerSelectionList) return 32;
        return value;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", ordinal = 0), index = 6)
    public int noDownFFF(int value){
        if ((Object) this instanceof ServerSelectionList) return this.y1 - 32;
        return value;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(Lnet/minecraft/client/renderer/RenderType;IIIIIII)V", ordinal = 0), index = 2)
    public int noDownF(int value){
        if ((Object) this instanceof ServerSelectionList) return 32;
        return value;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(Lnet/minecraft/client/renderer/RenderType;IIIIIII)V", ordinal = 0), index = 4)
    public int noDownFF(int value){
        if ((Object) this instanceof ServerSelectionList) return 32+4;
        return value;
    }
}
