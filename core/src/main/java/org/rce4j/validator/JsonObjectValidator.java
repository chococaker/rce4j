package org.rce4j.validator;

import net.chococaker.jjason.JsonElement;
import net.chococaker.jjason.JsonObject;

import java.util.Map;
import java.util.Set;

public final class JsonObjectValidator implements JsonValidator {
    public JsonObjectValidator(Map<String, JsonValidator> map) {
        this.map = Map.copyOf(map);
    }
    
    private final Map<String, JsonValidator> map;
    
    @Override
    public boolean test(JsonElement value) {
        if (!(value instanceof JsonObject object))
            return false;
        
        Set<String> keys = object.keySet();
        for (Map.Entry<String, JsonValidator> entry : map.entrySet()) {
            if (!keys.remove(entry.getKey()) || !entry.getValue().test(object.get(entry.getKey()))) {
                return false;
            }
        }
        
        return keys.isEmpty();
    }
}
