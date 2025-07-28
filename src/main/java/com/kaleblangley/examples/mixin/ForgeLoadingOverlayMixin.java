package com.kaleblangley.examples.mixin;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraftforge.client.loading.ForgeLoadingOverlay;
import net.minecraftforge.fml.earlydisplay.DisplayWindow;
import net.minecraftforge.fml.loading.progress.ProgressMeter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ForgeLoadingOverlay.class)
public class ForgeLoadingOverlayMixin {
    @Shadow @Final private ReloadInstance reload;

    @Shadow @Final private ProgressMeter progress;

    @Shadow @Final private Consumer<Optional<Throwable>> onFinish;

    @Shadow @Final private Minecraft minecraft;

    @Shadow private long fadeOutStart;

    @Shadow @Final private DisplayWindow displayWindow;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void cancel(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.reload.isDone()) {
            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable throwable) {
                this.onFinish.accept(Optional.of(throwable));
            }

            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
            }
        }
        ci.cancel();
    }
}
