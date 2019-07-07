package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SynthControlContainer extends JPanel {

    public static final Border LINE_BORDER = BorderFactory.createLineBorder(Color.BLACK);

    private Point mouseClickLocation;
    private final Synthesizer synthesizer;
    protected boolean on;

    public SynthControlContainer(Synthesizer synthesizer) {
        this.synthesizer = synthesizer;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public Point getMouseClickLocation() {
        return mouseClickLocation;
    }

    public void setMouseClickLocation(Point mouseClickLocation) {
        this.mouseClickLocation = mouseClickLocation;
    }

    @Override
    public Component add(Component comp) {
        comp.addKeyListener(synthesizer.getKeyAdapter());
        return super.add(comp);
    }

    @Override
    public Component add(String name, Component comp) {
        comp.addKeyListener(synthesizer.getKeyAdapter());
        return super.add(name, comp);
    }

    @Override
    public Component add(Component comp, int index) {
        comp.addKeyListener(synthesizer.getKeyAdapter());
        return super.add(comp, index);
    }

    @Override
    public void add(Component comp, Object constraints) {
        comp.addKeyListener(synthesizer.getKeyAdapter());
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        comp.addKeyListener(synthesizer.getKeyAdapter());
        super.add(comp, constraints, index);
    }

}
