package org.rce4j.module;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.rce4j.util.ArrayUtil.arrayContains;

public class ModulePath {
    public ModulePath(CharSequence chars) {
        String s = chars instanceof String ? (String) chars : chars.toString();
        
        String[] split = s.split("/");
        
        List<String> $strings = new ArrayList<>(split.length);
        
        for (String s1 : split) {
            if (s1.isEmpty())
                continue;
            
            validate(s1);
            $strings.add(s1);
        }
        
        strings = $strings.toArray(String[]::new);
    }
    
    public ModulePath(String... strings) {
        this.strings = new String[strings.length];
        
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            validate(s);
            this.strings[i] = s;
        }
    }
    
    public ModulePath(URI uri) {
        this(uri.getPath());
    }
    
    private static final char[] ALLOWED_SPECIAL_CHARS = {'-', '.', '_', '~', '!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@'};
    
    private final String[] strings;
    
    public String getPathPoint(int i) {
        return strings[i];
    }
    
    @Override
    public String toString() {
        return "/" + String.join("/", strings) + "/";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModulePath that = (ModulePath) o;
        return Arrays.equals(strings, that.strings);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(strings);
    }
    
    private static void validate(String s) {
        char[] chars = s.toCharArray();
        
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)
                    && !arrayContains(ALLOWED_SPECIAL_CHARS, c)) {
                throw new IllegalArgumentException("Invalid character in path '" + s + "' at col " + i + ": " + c);
            }
        }
    }
}
