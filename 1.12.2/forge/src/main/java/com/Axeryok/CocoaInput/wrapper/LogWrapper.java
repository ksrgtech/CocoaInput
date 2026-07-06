package com.Axeryok.CocoaInput.wrapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class LogWrapper {
    public static void log(String targetLog, Level level, String format, Object... data) {
        LogManager.getLogger(targetLog).log(level, String.format(format, data));
    }
}
