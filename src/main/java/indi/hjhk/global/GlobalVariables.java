package indi.hjhk.global;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariables {
    static Map<String, Object> globals=new HashMap<>();

    public static void putLong(String varName, Long value){
        globals.put(varName, value);
    }

    public static Long getLong(String varName){
        Object value=globals.get(varName);
        if (value instanceof Long longValue){
            return longValue;
        }
        return null;
    }
}
