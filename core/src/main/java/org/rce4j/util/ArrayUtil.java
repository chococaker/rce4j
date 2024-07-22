package org.rce4j.util;

public final class ArrayUtil {
    private ArrayUtil() {}
    
    public static boolean arrayContains(int[] ps, int p) {
        for (int tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static boolean arrayContains(byte[] ps, byte p) {
        for (byte tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static boolean arrayContains(short[] ps, short p) {
        for (short tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static boolean arrayContains(long[] ps, long p) {
        for (long tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static boolean arrayContains(float[] ps, float p) {
        for (float tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static boolean arrayContains(double[] ps, double p) {
        for (double tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static boolean arrayContains(boolean[] ps, boolean p) {
        for (boolean tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static boolean arrayContains(char[] ps, char p) {
        for (char tp : ps) {
            if (p == tp)
                return true;
        }
        
        return false;
    }
    
    public static <T> boolean arrayContains(T[] ts, T t) {
        for (T ti : ts) {
            if (t == ti || t.equals(ti)) // check for nullability then the actual values
                return true;
        }
        
        return false;
    }
}
