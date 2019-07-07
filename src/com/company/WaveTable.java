package com.company;

import static com.company.Synthesizer.AudioInfo.SAMPLE_RATE;

public enum WaveTable {

    SINE, SQUARE, SAW, TRIANGLE;

    static final int SIZE = 8192;

    private final float[] samples = new float[SIZE];

    static {
        final double FUND_FREQ = 1d / (SIZE / (double) SAMPLE_RATE);
        for (int i = 0; i < SIZE; ++i) {
            double t = i / (double) (SAMPLE_RATE);
            double tDivP = t / (1d / FUND_FREQ);
            SINE.samples[i] = (float) Math.sin(Main.frequencyToAngular(FUND_FREQ) * t);
            SQUARE.samples[i] = Math.signum(SINE.samples[i]);
            SAW.samples[i] = (float) (2d * (tDivP - Math.floor(0.5 + tDivP)));
            TRIANGLE.samples[i] = (float) (2d * Math.abs(SAW.samples[i]) - 1d);
        }
    }

    public float[] getSamples() {
        return samples;
    }
}
