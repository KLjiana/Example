package com.kaleblangley.wrench_plus;

import net.minecraftforge.fml.common.Mod;
import com.kaleblangley.wrench_plus.config.WrenchConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Mod(WrenchPlus.MODID)
public class WrenchPlus {
    public static final String MODID = "wrench_plus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


    public WrenchPlus() {
        WrenchConfig.load();
    }
}
