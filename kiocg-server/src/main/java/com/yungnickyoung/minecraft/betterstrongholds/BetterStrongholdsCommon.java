package com.yungnickyoung.minecraft.betterstrongholds;

import com.yungnickyoung.minecraft.betterstrongholds.module.ConfigModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterStrongholdsCommon {
    public static final String MOD_ID = "betterstrongholds";
    public static final String MOD_ID_ = "betterstrongholds:";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ConfigModule CONFIG = new ConfigModule();
}
