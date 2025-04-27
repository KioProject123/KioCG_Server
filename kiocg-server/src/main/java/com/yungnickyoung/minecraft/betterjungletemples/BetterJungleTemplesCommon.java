package com.yungnickyoung.minecraft.betterjungletemples;

import com.yungnickyoung.minecraft.betterjungletemples.module.ConfigModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterJungleTemplesCommon {
    public static final String MOD_ID = "betterjungletemples";
    public static final String MOD_ID_ = "betterjungletemples:";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigModule CONFIG = new ConfigModule();
}
