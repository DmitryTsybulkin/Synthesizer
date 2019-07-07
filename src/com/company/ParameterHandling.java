package com.company;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import static com.company.Main.invokeProcedure;

public class ParameterHandling {

    public static Robot PARAMETER_ROBOT;

    static {
        try {
            PARAMETER_ROBOT = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void addParameterMouseListener(Component component, SynthControlContainer container, int minVal,
                                                 int maxVal, int valStep, RefWrapper<Integer> parameter,
                                                 Procedure onChangeProcedure) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final Cursor BLACK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                        new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                        new Point(0,0), "blank_cursor");
                component.setCursor(BLACK_CURSOR);
                container.setMouseClickLocation(e.getLocationOnScreen());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                component.setCursor(Cursor.getDefaultCursor());
            }
        });
        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (container.getMouseClickLocation().y != e.getYOnScreen()) {
                    boolean mouseMoveUp = container.getMouseClickLocation().y - e.getYOnScreen() > 0;
                    if (mouseMoveUp && parameter.val < maxVal) {
                        parameter.val += valStep;
                    } else if (!mouseMoveUp && parameter.val > minVal) {
                        parameter.val -= valStep;
                    }
                    if (onChangeProcedure != null) {
                        invokeProcedure(onChangeProcedure, true);
                    }
                    PARAMETER_ROBOT.mouseMove(container.getMouseClickLocation().x, container.getMouseClickLocation().y);
                }
            }
        });
    }
}
