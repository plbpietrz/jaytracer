package org.rhx.graphics.jaytracer.util;

public class SimpleRNG {

    private static final float ONE_OVER_MAX_INT = 1.f / Integer.MAX_VALUE;
    private static final int MASK = (1 << 31) - 1;

    private static SimpleRNG instance;

    private int value;

    public static SimpleRNG get() {
        if (SimpleRNG.instance == null) {
            instance = new SimpleRNG();
        }
        return instance;
    }

    public int nextInt() {
        value = (value * 1_103_515_245 + 12_345) & MASK;
        return value;
    }

    public float nextFloat() {
        int u = nextInt();
        return u * ONE_OVER_MAX_INT;
    }

}
