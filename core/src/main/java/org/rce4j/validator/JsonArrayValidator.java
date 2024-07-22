package org.rce4j.validator;

import net.chococaker.jjason.JsonArray;
import net.chococaker.jjason.JsonElement;

public final class JsonArrayValidator implements JsonValidator {
    public JsonArrayValidator(int elementCount) {
        this(elementCount, null);
    }
    
    // completeValidator is applied to all elements
    public JsonArrayValidator(JsonValidator completeValidator) {
        this(-1, completeValidator);
    }
    
    public JsonArrayValidator(int elementCount, JsonValidator completeValidator) {
        this.elementCount = elementCount;
        this.completeValidator = completeValidator;
    }
    
    private final int elementCount;
    private final JsonValidator completeValidator;
    
    @Override
    public boolean test(JsonElement value) {
        if (!(value instanceof JsonArray array))
            return false;
        
        if (elementCount >= 0 && elementCount != array.size()) {
            return false;
        }
        
        if (completeValidator != null) {
            for (JsonElement element : array) {
                if (!completeValidator.test(element))
                    return false;
            }
        }
        
        return true;
    }
}
