package com.snek.frameworklib.debug;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.containers.Triplet;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.InteractionBlocker;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;
import com.snek.frameworklib.graphics.interfaces.Scrollable;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;








public class GraphicsDebugWindow extends JPanel {
    private static GraphicsDebugWindow w = null;
    private static JFrame frame;
    public static @NotNull GraphicsDebugWindow getW() { return w; }
    public static @NotNull JFrame getFrame() { return frame; }
    private Point cursorPosition = null;


    static {
        if(DebugCheck.isDebug()) {
            System.setProperty("sun.java2d.opengl", "true");
            System.setProperty("sun.java2d.opengl.fbobject", "false");
            System.setProperty("sun.java2d.d3d", "false");
            System.setProperty("sun.java2d.noddraw", "true");
            System.setProperty("sun.java2d.pmoffscreen", "false");

            w = new GraphicsDebugWindow();
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



    @SuppressWarnings("java:S4144") //! Identical definitions
    public GraphicsDebugWindow() {

        // Track cursor movement
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cursorPosition = e.getPoint();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                cursorPosition = e.getPoint();
                repaint();
            }
        });
    }




    private static @NotNull Color currentColor = Color.WHITE;
    private static @NotNull Div currentElm = null;
    private final @NotNull List<@NotNull Triplet<Vector2f, Color, Div>> vertices = new ArrayList<>();


    public static void changeColor(final @NotNull Color newColor) {
        assert Require.nonNull(newColor, "new color");
        currentColor = newColor;
    }
    public static void changeElm(final @NotNull Div newElm) {
        assert Require.nonNull(newElm, "new element");
        currentElm = newElm;
    }

    public void add(final @NotNull Vector2f v) {
        assert Require.nonNull(v, "vector");
        vertices.add(Triplet.from(v, currentColor, currentElm));
    }

    public void clear() {
        vertices.clear();
    }




    @Override
    protected void paintComponent(final @NotNull Graphics g) {
        assert Require.nonNull(g, "graphics object");

        super.paintComponent(g);
        final Graphics2D _g = (Graphics2D)g;
        _g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        _g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        final int width = getWidth();
        final int height = getHeight();
        final int centerX = width / 2;
        final int centerY = height / 2;


        Div renderedElm = null;
        final int l = (int)(Math.min(width, height) * 0.5f);
        for(int i = 0; i < vertices.size(); i += 4) {
            try {
                final int x1 = centerX - (int)(l * vertices.get(i + 0).getFirst().x);
                final int y1 = centerY - (int)(l * vertices.get(i + 0).getFirst().y);
                final int x2 = centerX - (int)(l * vertices.get(i + 1).getFirst().x);
                final int y2 = centerY - (int)(l * vertices.get(i + 1).getFirst().y);
                final int x3 = centerX - (int)(l * vertices.get(i + 2).getFirst().x);
                final int y3 = centerY - (int)(l * vertices.get(i + 2).getFirst().y);
                final int x4 = centerX - (int)(l * vertices.get(i + 3).getFirst().x);
                final int y4 = centerY - (int)(l * vertices.get(i + 3).getFirst().y);


                // Fill rectangles
                final Color color = vertices.get(i).getSecond();
                _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
                _g.setColor(new Color(
                    (int)(color.getRed()   * 0.2),
                    (int)(color.getGreen() * 0.2),
                    (int)(color.getBlue()  * 0.2)
                ));
                _g.fillPolygon(new int[]{ x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 }, 4);


                // Draw outlines
                _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
                _g.setColor(vertices.get(i).getSecond());
                _g.drawPolygon(new int[]{ x1, x2, x3, x4 }, new int[] { y1, y2, y3, y4 }, 4);


                // Find targeted element
                final Div elm = vertices.get(i).getThird();
                if(elm != null && cursorPosition != null) {
                    Polygon polygon = new Polygon();
                    polygon.addPoint(x1, y1);
                    polygon.addPoint(x2, y2);
                    polygon.addPoint(x3, y3);
                    polygon.addPoint(x4, y4);
                    if(polygon.contains(cursorPosition)) {
                        renderedElm = elm;
                    }
                }
            }
            catch(IndexOutOfBoundsException e) {
                //! Empty

                //! Sometimes the debug window and the list of vertices aren't properly coordinated,
                //! so getting vertices by hard coded index can cause out-of-bounds accesses.

                //! This is not worth fixing as the debug window is only used as a visual aid during development
                //! and the problem doesn't have any side effect and usually fixes itself within a frame.
            }
        }




        // Draw info lines
        final int fontSize = 16;
        final int lineDist = 5;
        final int borderDist = 10;
        _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));
        _g.setColor(Color.WHITE);
        _g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        final FontMetrics fm = _g.getFontMetrics();

        // True targeted elm info (targeted by the player in game, left side)
        final var a = Context.getActiveContexts().values().iterator();
        final var b = a.hasNext() ? a.next() : null;
        final Context context = (b == null || b.isEmpty()) ? null : b.get(0);
        final String[] targetedElmLines = computeElmInfoLines(
            context == null ? null : context.getTargetedElm(),
            context == null ? "-" : context.getPlayer().getName().getString()
        );
        for(int i = 0; i < targetedElmLines.length; ++i) {
            final String line = targetedElmLines[i];
            _g.drawString(line, width - fm.stringWidth(line) - borderDist, (fontSize + borderDist) + (fontSize + lineDist) * i);
        }

        // Rendered elm info (targeted by the cursor in the window, right side)
        final String[] renderedElmLines = computeElmInfoLines(renderedElm, "Debug window");
        for(int i = 0; i < renderedElmLines.length; ++i) {
            final String line = renderedElmLines[i];
            _g.drawString(line, borderDist, (fontSize + borderDist) + (fontSize + lineDist) * i);
        }

        // Render stats
        final String[] statsLines = {
            "Players: " + FrameworkLib.getServer().getPlayerCount(),
            "Active contexts:  " + Context.getActiveContexts().size(),
            "Display entities: " + getEntityCount(Elm.ENTITY_CUSTOM_NAME),
            "Hitbox entities:  " + getEntityCount(InteractionBlocker.ENTITY_CUSTOM_NAME),
        };
        for(int i = statsLines.length; i >= 0; --i) {
            final String line = statsLines[i];
            _g.drawString(line, borderDist, height - borderDist - (fontSize + lineDist) * (statsLines.length - 1 - i));
        }




        // Draw cursor center
        if(cursorPosition != null) {
            _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            _g.setColor(Color.WHITE);
            _g.drawLine(cursorPosition.x, 0, cursorPosition.x, height);
            _g.drawLine(0, cursorPosition.y, width, cursorPosition.y);
        }

        // Draw in-game center
        _g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        _g.setColor(Color.WHITE);
        _g.drawLine(centerX, 0, centerX, height);
        _g.drawLine(0, centerY, width, centerY);
    }







    private static final String[] computeElmInfoLines(final @Nullable Div elm, final @NotNull String sourceName) {
        if(elm == null) {
            return new String[]{};
        }
        return new String[]{
            elm.getClass().getSimpleName(),
            "SOURCE: " + sourceName,
            "Path: " + elm.getClass().getCanonicalName(),
            "Style: " + (elm instanceof Elm e ? ("" + e.getStyle().getClass().getCanonicalName()) : "-"),
            "",
            "Hoverable: " + (elm instanceof Hoverable ? "yes" : "no"),
            "Clickable: " + (elm instanceof Clickable ? "yes" : "no"),
            "Scrollable: " + (elm instanceof Scrollable ? "yes" : "no"),
            "",
            "World origin: " + getVectorString(elm.__calcVisualOrigin()),
            "Hovered: " + (elm.isHovered() ? "yes" : "no"),
            "Data queue: " + (elm instanceof Elm e ? ("" + e.getDataQueueSize()) : "-"),
            "",
            "Alignment: (" + elm.getAlignmentX().name() + ", " + elm.getAlignmentY().name() + ")",
            "RelPos: " + getVectorString(elm.getAbsPos()),
            "AbsPos: " + getVectorString(elm.getLocalPos()),
            "RelSize: " + getVectorString(elm.getAbsSize()),
            "AbsSize: " + getVectorString(elm.getLocalSize()),
        };
    }



    private static String getVectorString(final @NotNull Vector3f v) {
        return "(" + String.format("(%.4f, %.4f, %.4f)", v.x, v.y, v.z) + ")";
    }
    private static String getVectorString(final @NotNull Vector2f v) {
        return "(" + String.format("(%.4f, %.4f)", v.x, v.y) + ")";
    }


    private static int getEntityCount(final @NotNull String customName) {
        int r = 0;
        for(final ServerLevel level : FrameworkLib.getServer().getAllLevels()) {
            for(final Entity entity : level.getAllEntities()) {
                if(entity.getCustomName().getString().equals(customName)) ++r;
            }
        }
        return r;
    }
}