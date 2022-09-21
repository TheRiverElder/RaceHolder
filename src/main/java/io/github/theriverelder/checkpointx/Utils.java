package io.github.theriverelder.checkpointx;

import org.bukkit.util.Vector;

public class Utils {
    public static String toFixedString(double num, int fractions) {
        return String.format("%." + Math.max(0, fractions) + "f", num);
    }

    public static String toFixedString(double num) {
        return toFixedString(num, 2);
    }

    public static String toFixedString(Vector vector, int fractions) {
        return "(" + toFixedString(vector.getX(), fractions) + ", " + toFixedString(vector.getY(), fractions) + ", " + toFixedString(vector.getZ(), fractions) + ")";
    }

    public static String toFixedString(Vector vector) {
        return "(" + toFixedString(vector.getX()) + ", " + toFixedString(vector.getY()) + ", " + toFixedString(vector.getZ()) + ")";
    }
}
