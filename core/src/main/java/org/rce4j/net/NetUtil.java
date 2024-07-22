package org.rce4j.net;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class NetUtil {
    private NetUtil() {}
    
    @Deprecated
    public static Map<String, String> queryParams(String s) {
        char[] chars = s.toCharArray();
        
        boolean isOnParams = false;
        boolean isOnValue = false;
        
        Map<String, String> params = null;
        StringBuilder key = null;
        StringBuilder val = null;
        
        for (char c : chars) {
            if (!isOnParams && c != '?') {
                continue;
            } else if (!isOnParams) { // c == '?' and is not on params
                isOnParams = true;
                params = new HashMap<>();
                key = new StringBuilder();
                val = new StringBuilder();
                continue;
            }
            
            if (c == '&') {
                params.put(key.toString(), val.toString());
                key = new StringBuilder();
                val = new StringBuilder();
                isOnValue = false;
                continue;
            }
            
            if (c == '=') {
                val = new StringBuilder();
                isOnValue = true;
            }
            
            if (isOnValue)
                val.append(c);
            else
                key.append(c);
        }
        
        return params == null ? Map.of() : Collections.unmodifiableMap(params);
    }
}
