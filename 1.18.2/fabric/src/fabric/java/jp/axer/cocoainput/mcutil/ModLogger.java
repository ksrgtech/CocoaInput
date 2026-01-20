package jp.axer.cocoainput.util;

import jp.axer.cocoainput.domain;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class ModLogger implements SimpleLogger {
    public static boolean debugMode=true;

    public void log(String msg,Object...data){
        LogManager.getLogger("CocoaInput:Java").log( Level.INFO,msg, data);
    }

    public void error(String msg,Object...data){
        LogManager.getLogger("CocoaInput:Java").error(msg, data);
    }

    public void debug(String msg,Object...data){
        if(debugMode){
            LogManager.getLogger("CocoaInput:Java").debug(msg, data);
        }
    }
}
