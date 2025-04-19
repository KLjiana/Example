package com.kaleblangley.examples;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

/**
 * @author tterrag, Cheaterpaul
 * <a href="https://github.com/Cheaterpaul/ReBlured">source</a>
 * */
@Mod(Blur.MODID)
public class Blur {

    public static final String MODID = "examples";


    public Blur() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "", (incoming, isNetwork) -> true));
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            BlurConfig.registerConfig();
            BlurClient.register();
        });
    }
}
