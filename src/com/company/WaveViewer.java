package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

import static com.company.SynthControlContainer.LINE_BORDER;

public class WaveViewer extends JPanel {

    private Oscillator[] oscillators;

    public WaveViewer(Oscillator[] oscillators) {
        this.oscillators = oscillators;
        setBorder(LINE_BORDER);
        setBounds(320, 0,420,420);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = 30;
        int midY = getHeight() / 2;
        int numSamples = getWidth() - pad * 2;
        double[] mixedSamples = new double[numSamples];
        for (Oscillator oscillator : oscillators) {
            double[] samples = oscillator.getSampleWaveForm(numSamples);
            for (int i = 0; i < samples.length; i++) {
                mixedSamples[i] += samples[i] / oscillators.length;
            }
        }
        graphics2D.drawLine(pad, midY, getWidth() - pad, midY);
        graphics2D.drawLine(pad, pad, pad, getHeight() - pad);

        Function<Double, Integer> sampleToCoordinate = sample -> (int) (midY + sample * (midY - pad));
        for (int i = 0; i < numSamples; i++) {
            int nextY = i == numSamples - 1 ? sampleToCoordinate.apply(mixedSamples[i]) : sampleToCoordinate.apply(mixedSamples[i + 1]);
            graphics2D.drawLine(pad + i, sampleToCoordinate.apply(mixedSamples[i]), pad + i + 1, nextY);
        }
    }
}
