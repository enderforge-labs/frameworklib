package com.snek.frameworklib.debug;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.containers.Pair;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

    private static @NotNull Color currentColor = Color.WHITE;
    private final @NotNull List<@NotNull Pair<@NotNull Vector2f, @NotNull Color>> vertices = new ArrayList<>();


    public static void changeColor(final @NotNull Color newColor) {
        assert Require.nonNull(newColor, "new color");
        currentColor = newColor;
    }

    public void add(final @NotNull Vector2f v) {
        assert Require.nonNull(v, "vector");
        vertices.add(Pair.from(v, currentColor));
    }

    public void clear() {
        vertices.clear();
    }




    @Override
    protected void paintComponent(final @NotNull Graphics _g) {
        assert Require.nonNull(_g, "graphics object");

        super.paintComponent(_g);
        final Graphics2D g = (Graphics2D)_g;
        final int width = getWidth();
        final int height = getHeight();
        final int centerX = width / 2;
        final int centerY = height / 2;


        // Fill rectangles
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        for(int i = 0; i < vertices.size(); i += 4) {
            final int x1 = centerX - (int)(400 * vertices.get(i + 0).getFirst().x);
            final int y1 = centerY - (int)(400 * vertices.get(i + 0).getFirst().y);
            final int x2 = centerX - (int)(400 * vertices.get(i + 1).getFirst().x);
            final int y2 = centerY - (int)(400 * vertices.get(i + 1).getFirst().y);
            final int x3 = centerX - (int)(400 * vertices.get(i + 2).getFirst().x);
            final int y3 = centerY - (int)(400 * vertices.get(i + 2).getFirst().y);
            final int x4 = centerX - (int)(400 * vertices.get(i + 3).getFirst().x);
            final int y4 = centerY - (int)(400 * vertices.get(i + 3).getFirst().y);

            final Color color = vertices.get(i).getSecond();
            g.setColor(new Color(
                (int)(color.getRed() * 0.2),
                (int)(color.getGreen() * 0.2),
                (int)(color.getBlue() * 0.2)
            ));
            g.fillPolygon(new int[]{ x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 }, 4);
        }


        // Draw outlines
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        for(int i = 0; i < vertices.size(); i += 4) {
            final int x1 = centerX - (int)(400 * vertices.get(i + 0).getFirst().x);
            final int y1 = centerY - (int)(400 * vertices.get(i + 0).getFirst().y);
            final int x2 = centerX - (int)(400 * vertices.get(i + 1).getFirst().x);
            final int y2 = centerY - (int)(400 * vertices.get(i + 1).getFirst().y);
            final int x3 = centerX - (int)(400 * vertices.get(i + 2).getFirst().x);
            final int y3 = centerY - (int)(400 * vertices.get(i + 2).getFirst().y);
            final int x4 = centerX - (int)(400 * vertices.get(i + 3).getFirst().x);
            final int y4 = centerY - (int)(400 * vertices.get(i + 3).getFirst().y);
            g.setColor(vertices.get(i).getSecond());
            g.drawPolygon(new int[]{ x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 }, 4);
        }


        // Draw center
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.setColor(Color.WHITE);
        g.drawLine(centerX, 0, centerX, height);
        g.drawLine(0, centerY, width, centerY);
    }
}