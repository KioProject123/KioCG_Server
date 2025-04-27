package com.yungnickyoung.minecraft.betterfortresses;

import com.yungnickyoung.minecraft.betterfortresses.module.ConfigModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterFortressesCommon {
    public static final String MOD_ID = "betterfortresses";
    public static final String MOD_ID_ = "betterfortresses:";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigModule CONFIG = new ConfigModule();
}
