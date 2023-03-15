package indi.hjhk.log;

import com.mysql.cj.log.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Logger {
    static SimpleDateFormat fileNameFormat = new SimpleDateFormat("yy_MM_dd HH_mm");
    static SimpleDateFormat timeStampFormat = new SimpleDateFormat("[yy/MM/dd HH:mm:ss:SSS] ");
    static final File logFile = new File("./hjhkLog/" + fileNameFormat.format(System.currentTimeMillis()) + ".txt");
    static FileWriter logWriter;
    static boolean able = false;
    static boolean autoFlush = true;
    static boolean failed = false;
    static String strNewline = "\n";
    static String strLogNewline = strNewline + "                        ";

    private static boolean init() {
        if (able)
            return true;
        if (failed)
            return false;
        able = true;
        try {
            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            logWriter = new FileWriter(logFile);
        } catch (IOException e) {
            e.printStackTrace();
            able = false;
            return false;
        }
        //logSilent("log start.");
        return true;
    }

    public static void enableAutoFlush(){
        autoFlush = true;
    }

    public static void disableAutoFlush(){
        autoFlush = false;
    }

    private static void logRawContent(String logConent) {
        if (failed) return;
        synchronized (logFile) {
            try {
                int count=0;
                while (!init()){
                    count++;
                    if (count>=3){
                        failed = true;
                        System.err.println("[hjhkLogError] failed to init hjhkLog.");
                        return;
                    }
                }
                logWriter.write(timeStampFormat.format(System.currentTimeMillis()) + logConent + "\n");
                if (autoFlush) logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logSilent(String str, Object... args){
        logRawContent(String.format(str, args).replaceAll(strNewline, strLogNewline));
    }

    public static void logOnStdout(String str, Object... args){
        String logContent = String.format(str, args);
        logRawContent(logContent.replaceAll(strNewline, strLogNewline));
        System.out.println(logContent);
    }

    public static void logOnStderr(String str, Object... args){
        String logContent = String.format(str, args);
        logRawContent(logContent.replaceAll(strNewline, strLogNewline));
        System.err.println(logContent);
    }

    public static void flush(){
        if (failed) return;
        synchronized (logFile) {
            try {
                int count=0;
                while (!init()){
                    count++;
                    if (count>=3){
                        failed = true;
                        System.err.println("[hjhkLogError] failed to init hjhkLog.");
                        return;
                    }
                }
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
