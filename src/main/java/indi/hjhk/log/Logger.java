package indi.hjhk.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Logger {
    static SimpleDateFormat dirFormat = new SimpleDateFormat("yy_MM_dd HH_mm");
    static SimpleDateFormat timeStampFormat = new SimpleDateFormat("[yy/MM/dd HH:mm:ss:SSS] ");
    static final File logFile = new File("./hjhkLog/" + dirFormat.format(System.currentTimeMillis()) + ".txt");
    static FileWriter logWriter;
    static boolean able = false;

    private static boolean init() {
        if (able)
            return true;
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
        logSilent("log start.");
        return true;
    }

    public static void logSilent(String str, Object... args) {
        String logContent = String.format(str, args);
        synchronized (logFile) {
            try {
                int count=0;
                while (!init()){
                    count++;
                    if (count>=3){
                        System.err.println("failed to init hjhkLog.");
                    }
                }
                logWriter.write(timeStampFormat.format(System.currentTimeMillis()) + logContent + "\n");
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logOnStdout(String str, Object... args){
        String logContent = String.format(str, args);
        logSilent(logContent);
        System.out.println(logContent);
    }

    public static void logOnStderr(String str, Object... args){
        String logContent = String.format(str, args);
        logSilent(logContent);
        System.err.println(logContent);
    }
}
