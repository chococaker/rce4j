package org.rce4j.validator;

import net.chococaker.jjason.JsonArray;
import net.chococaker.jjason.JsonElement;
import net.chococaker.jjason.JsonObject;
import net.chococaker.jjason.JsonPrimitive;

public interface JsonValidator {
    JsonValidator BASIC_ARRAY_VALIDATOR = e -> e instanceof JsonArray;
    JsonValidator BASIC_PRIMITIVE_VALIDATOR = e -> e instanceof JsonPrimitive;
    JsonValidator BASIC_OBJECT_VALIDATOR = e -> e instanceof JsonObject;
    JsonValidator BASIC_EXISTENCE_VALIDATOR = e -> true;
    
    boolean test(JsonElement value);
}
