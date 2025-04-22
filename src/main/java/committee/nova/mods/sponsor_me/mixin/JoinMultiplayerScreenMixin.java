package committee.nova.mods.sponsor_me.mixin;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerScreenMixin {
    @ModifyConstant(method = "init", constant = @Constant(intValue = 32))
    public int noAdd(int constant){
        return 64;
    }
}
