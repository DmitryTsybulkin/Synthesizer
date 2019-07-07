package com.company;

public class Main {

    public static void main(String[] args) {
        new Synthesizer();
    }

    public static void invokeProcedure(Procedure procedure, boolean printStackTrace) {
        try {
            procedure.invoke();
        } catch (Exception e) {
            if (printStackTrace) e.printStackTrace();
        }
    }

    public static double frequencyToAngular(double freq) {
        return 2 * Math.PI * freq;
    }

    public static double getKeyFrequency(int keyNum) {
        return Math.pow(root(2, 12), keyNum - 49) * 440;
    }

    public static double root(double num, double root) {
        return Math.pow(Math.E, Math.log(num) / root);
    }

    public static double offsetTone(double baseFreq, double freqMultiplier) {
        return baseFreq * Math.pow(2.0, freqMultiplier);
    }

}
