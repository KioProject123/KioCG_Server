package com.kiocg.meta;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface TemporaryDataHolder {
    @NotNull
    Map<String, Object> getTemporaryMeta();
}
