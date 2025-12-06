package com.snek.frameworklib.debug;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.utils.Utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;








public class UiDebugWindow extends JPanel {
    private static UiDebugWindow w = null;
    private static JFrame frame;
    public static @NotNull UiDebugWindow getW() { return w; }
    public static @NotNull JFrame getFrame() { return frame; }

    static {
        if(DebugCheck.isDebug()) {
            System.setProperty("sun.java2d.opengl", "true");
            System.setProperty("sun.java2d.opengl.fbobject", "false");
            System.setProperty("sun.java2d.d3d", "false");
            System.setProperty("sun.java2d.noddraw", "true");
            System.setProperty("sun.java2d.pmoffscreen", "false");

            w = new UiDebugWindow();
            frame = new JFrame("FrameworkLib - Graphics debug window");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);

            // Set layout explicitly
            frame.setLayout(new BorderLayout());
            frame.add(w, BorderLayout.CENTER);
            w.setBackground(Color.BLACK);

            // Set preferred size and pack the frame
            frame.setPreferredSize(new Dimension(400, 300));
            frame.pack();

            // Make the frame visible and set buffer strategy
            frame.setVisible(true);
            frame.createBufferStrategy(2);
            w.setDoubleBuffered(true);
        }
    }


    private final @NotNull List<@NotNull Vector2f> vertices = new ArrayList<>();
    public void add(final @NotNull Vector2f v) {
        vertices.add(v);
    }
    public void clear() {
        vertices.clear();
    }


    @Override
    protected void paintComponent(final @NotNull Graphics g) {
        super.paintComponent(g);
        final int width = getWidth();
        final int height = getHeight();
        final int centerX = width / 2;
        final int centerY = height / 2;

        // Draw rectangles
        g.setColor(Color.GREEN);
        for(int i = 0; i < vertices.size(); i += 4) {
            final int x1 = centerX - (int)(400 * vertices.get(i + 0).x);
            final int y1 = centerY - (int)(400 * vertices.get(i + 0).y);
            final int x2 = centerX - (int)(400 * vertices.get(i + 1).x);
            final int y2 = centerY - (int)(400 * vertices.get(i + 1).y);
            final int x3 = centerX - (int)(400 * vertices.get(i + 2).x);
            final int y3 = centerY - (int)(400 * vertices.get(i + 2).y);
            final int x4 = centerX - (int)(400 * vertices.get(i + 3).x);
            final int y4 = centerY - (int)(400 * vertices.get(i + 3).y);
            g.drawPolygon(new int[]{ x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 }, 4);
        }

        // Draw center
        g.setColor(Color.WHITE);
        g.drawLine(centerX, 0, centerX, height);
        g.drawLine(0, centerY, width, centerY);
    }
}