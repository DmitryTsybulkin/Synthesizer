package com.company;

import javax.swing.*;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import static com.company.AudioThread.BUFFER_SIZE;
import static com.company.Main.getKeyFrequency;

public class Synthesizer {

    private static final Map<Character, Double> KEY_FREQUENCIES = new HashMap<>();

    private boolean shouldGenerate;
    private final Oscillator[] oscillators = new Oscillator[3];
    private final JFrame jFrame = new JFrame("The best synthesizer");
    private final WaveViewer waveViewer = new WaveViewer(oscillators);

    static {
        final int STARTING_KEY = 16;
        final int KEY_FREQUENCY_INCR = 2;
        final char[] KEYS = "zxcvbnm,./asdfghjkl;'qwertyuiop[]".toCharArray();
        for (int i = STARTING_KEY, key = 0;
             i < KEYS.length * KEY_FREQUENCY_INCR + STARTING_KEY;
             i += KEY_FREQUENCY_INCR, ++key) {
                KEY_FREQUENCIES.put(KEYS[key], getKeyFrequency(i));
        }
    }

    public Synthesizer() {
        int y = 0;
        for (int i = 0; i < oscillators.length; i++) {
            this.oscillators[i] = new Oscillator(this);
            this.oscillators[i].setLocation(5, y);
            jFrame.add(oscillators[i]);
            y+=105;
        }

        jFrame.add(waveViewer);
        jFrame.addKeyListener(keyAdapter);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                audioThread.close();
            }
        });
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setSize(755, 460);
        jFrame.setResizable(false);
        jFrame.setLayout(null);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    private final AudioThread audioThread = new AudioThread(() -> {
        if (!shouldGenerate) return null;
        short[] arr = new short[BUFFER_SIZE];
        for (int i = 0; i < BUFFER_SIZE; i++) {
            double sample = 0;
            for (Oscillator oscillator : oscillators) {
                sample += oscillator.nextSample() / oscillators.length;
            }
            arr[i] = (short) (Short.MAX_VALUE * sample);
        }
        return arr;
    });

    private final KeyListener keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!KEY_FREQUENCIES.containsKey(e.getKeyChar())) {
                return;
            }
            if (!audioThread.isRunning()) {
                for (Oscillator oscillator : oscillators) {
                    oscillator.setKeyFrequency(KEY_FREQUENCIES.get(e.getKeyChar()));
                }
                shouldGenerate = true;
                audioThread.triggeredPlayback();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            shouldGenerate = false;
        }
    };

    public void updateWaveViewer() {
        waveViewer.repaint();
    }

    public KeyListener getKeyAdapter() {
        return keyAdapter;
    }

    public static class AudioInfo {
        public static final int SAMPLE_RATE = 44100;
    }
}
