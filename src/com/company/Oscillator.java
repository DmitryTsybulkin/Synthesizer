package com.company;

import javax.swing.*;
import java.awt.event.ItemEvent;

import static com.company.Synthesizer.AudioInfo.SAMPLE_RATE;

public class Oscillator extends SynthControlContainer {

    private static final int TONE_OFFSET_LIMIT = 2000;
    public static final String TEXT_PADDING = "   ";

    private WaveTable waveTable = WaveTable.SINE;
    private RefWrapper<Integer> toneOffset = new RefWrapper<>(0);
    private RefWrapper<Integer> volume = new RefWrapper<>(100);
    private double keyFrequency;
    private int waveTableIndex;
    private int waveTableStepSize;

    public Oscillator(Synthesizer synthesizer) {
        super(synthesizer);
        JComboBox<WaveTable> comboBox = new JComboBox<>(WaveTable.values());
        comboBox.setSelectedItem(WaveTable.SINE);
        comboBox.setBounds(10,10,90,25);
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                waveTable = (WaveTable) e.getItem();
            }
            synthesizer.updateWaveViewer();
        });
        add(comboBox);

        JLabel toneParameter = new JLabel(TEXT_PADDING + "x0.00");
        toneParameter.setBounds(180, 65, 50, 25);
        toneParameter.setBorder(LINE_BORDER);
        add(toneParameter);

        ParameterHandling.addParameterMouseListener(toneParameter, this, -TONE_OFFSET_LIMIT, TONE_OFFSET_LIMIT,
                10, toneOffset, () -> {
            applyToneOffset();
            toneParameter.setText(TEXT_PADDING + "x" + getToneOffset());
            synthesizer.updateWaveViewer();
        });

        JLabel toneText = new JLabel(TEXT_PADDING + "Tone");
        toneText.setBounds(180, 30, 50, 25);
        toneText.setBorder(LINE_BORDER);
        add(toneText);

        JLabel volumeParameter = new JLabel(TEXT_PADDING + "100%");
        volumeParameter.setBounds(240, 65, 50, 25);
        volumeParameter.setBorder(LINE_BORDER);
        ParameterHandling.addParameterMouseListener(volumeParameter, this, 0, 100, 1,
                volume, () -> {
            volumeParameter.setText(TEXT_PADDING + volume.val + "%");
            synthesizer.updateWaveViewer();
        });
        add(volumeParameter);
        JLabel volumeText = new JLabel(" Volume");
        volumeText.setBounds(240, 30, 50, 25);
        volumeText.setBorder(LINE_BORDER);
        add(volumeText);

        setSize(300, 100);
        setBorder(LINE_BORDER);
        setLayout(null);
    }

    private double getToneOffset() {
        return toneOffset.val / 1000d;
    }

    private double getVolumeMultiplier() {
        return volume.val / 100.0;
    }

    public double[] getSampleWaveForm(int numSamples) {
        double[] samples = new double[numSamples];
        double freq = 1.0 / (numSamples / (double) SAMPLE_RATE) * 3.0;
        int index = 0;
        int stepSize = (int) (WaveTable.SIZE * Main.offsetTone(freq, getToneOffset()) / SAMPLE_RATE);
        for (int i = 0; i < numSamples; i++) {
            samples[i] = waveTable.getSamples()[index] * getVolumeMultiplier();
            index = (index + stepSize) % WaveTable.SIZE;
        }
        return samples;
    }

    public double nextSample() {
        double sample = waveTable.getSamples()[waveTableIndex] * getVolumeMultiplier();
        waveTableIndex = (waveTableIndex + waveTableStepSize) % WaveTable.SIZE;
        return sample;
    }

    public void applyToneOffset() {
        waveTableStepSize = (int) (WaveTable.SIZE * Main.offsetTone(keyFrequency, getToneOffset()) / SAMPLE_RATE);
    }

    public void setKeyFrequency(double frequency) {
        this.keyFrequency = frequency;
        applyToneOffset();
    }

}
