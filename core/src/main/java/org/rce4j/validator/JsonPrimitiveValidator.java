package org.rce4j.validator;

import net.chococaker.jjason.JsonElement;
import net.chococaker.jjason.JsonPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public final class JsonPrimitiveValidator implements JsonValidator {
    public <T> JsonPrimitiveValidator(Class<T> elementClass) {
        this(elementClass, null);
    }
    
    public <T> JsonPrimitiveValidator(Class<T> elementClass, Collection<T> validValues) {
        Objects.requireNonNull(elementClass);
        
        if (!PRIMITIVE_CLASSES.contains(elementClass))
            throw new IllegalArgumentException("Element class " + elementClass + " is not a valid JsonPrimitive class");
        this.elementClass = elementClass;
        
        if (validValues == null) {
            this.validValues = Set.of();
        } else {
            this.validValues = Set.copyOf(validValues);
        }
    }
    
    private static final Set<Class<?>> PRIMITIVE_CLASSES = Set.of(
            int.class, long.class, double.class, boolean.class,
            String.class, BigDecimal.class, BigInteger.class
    );
    
    private final Class<?> elementClass;
    
    private final Set<Object> validValues;
    
    @Override
    public boolean test(JsonElement value) {
        if (!(value instanceof JsonPrimitive primitive))
            return false;
        
        if (!elementClass.equals(primitive.get().getClass())) {
            return false;
        }
        
        if (validValues != null) {
            for (Object o : validValues) {
                if (Objects.equals(o, value)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }
}
