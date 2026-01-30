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
    protected void paintComponent(final @NotNull Graphics g) {
        assert Require.nonNull(g, "graphics object");

        super.paintComponent(g);
        final Graphics2D _g = (Graphics2D)g;
        final int width = getWidth();
        final int height = getHeight();
        final int centerX = width / 2;
        final int centerY = height / 2;


        // Fill rectangles
        _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        for(int i = 0; i < vertices.size(); i += 4) {
            try {
                final int x1 = centerX - (int)(400 * vertices.get(i + 0).getFirst().x);
                final int y1 = centerY - (int)(400 * vertices.get(i + 0).getFirst().y);
                final int x2 = centerX - (int)(400 * vertices.get(i + 1).getFirst().x);
                final int y2 = centerY - (int)(400 * vertices.get(i + 1).getFirst().y);
                final int x3 = centerX - (int)(400 * vertices.get(i + 2).getFirst().x);
                final int y3 = centerY - (int)(400 * vertices.get(i + 2).getFirst().y);
                final int x4 = centerX - (int)(400 * vertices.get(i + 3).getFirst().x);
                final int y4 = centerY - (int)(400 * vertices.get(i + 3).getFirst().y);

                final Color color = vertices.get(i).getSecond();
                _g.setColor(new Color(
                    (int)(color.getRed() * 0.2),
                    (int)(color.getGreen() * 0.2),
                    (int)(color.getBlue() * 0.2)
                ));
                _g.fillPolygon(new int[]{ x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 }, 4);
            }
            catch(IndexOutOfBoundsException e) {
                //! Empty

                //! Sometimes the debug window and the list of vertices aren't properly coordinated,
                //! so getting vertices by hard coded index can cause out-of-bounds accesses.

                //! This is not worth fixing as the debug window is only used as a visual aid during development
                //! and the problem doesn't have any side effect and usually fixes itself within a frame.
            }
        }


        // Draw outlines
        _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        for(int i = 0; i < vertices.size(); i += 4) {
            final int x1 = centerX - (int)(400 * vertices.get(i + 0).getFirst().x);
            final int y1 = centerY - (int)(400 * vertices.get(i + 0).getFirst().y);
            final int x2 = centerX - (int)(400 * vertices.get(i + 1).getFirst().x);
            final int y2 = centerY - (int)(400 * vertices.get(i + 1).getFirst().y);
            final int x3 = centerX - (int)(400 * vertices.get(i + 2).getFirst().x);
            final int y3 = centerY - (int)(400 * vertices.get(i + 2).getFirst().y);
            final int x4 = centerX - (int)(400 * vertices.get(i + 3).getFirst().x);
            final int y4 = centerY - (int)(400 * vertices.get(i + 3).getFirst().y);
            _g.setColor(vertices.get(i).getSecond());
            _g.drawPolygon(new int[]{ x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 }, 4);
        }


        // Draw center
        _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        _g.setColor(Color.WHITE);
        _g.drawLine(centerX, 0, centerX, height);
        _g.drawLine(0, centerY, width, centerY);
    }
}