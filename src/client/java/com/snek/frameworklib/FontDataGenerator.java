package com.snek.frameworklib;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;








/**
 * A class that calculates and writes the in-game width and height of each individual character that fits in a {@code char}.
 * <p>
 * These dimensions are based on the current active resource pack stack.
 * <p>
 * The values represent the number of pixels the character occupies on each axis.
 * <p>
 * TEXT_PIXEL_BLOCK_RATIO represents the amount of pixels that fit in a minecraft block when rendered through a text display.
 * This assumes the text display has scale (1, 1) and no shearing.
 */
public abstract class FontDataGenerator {
    private FontDataGenerator() {}
    public static final @NotNull String PACKAGE_NAME = "com.snek.frameworklib.generated";         // The name of the "generated" package
    public static final @NotNull String PACKAGE_PATH = "frameworklib/generated";                  // The path to the "generated" package
    public static final @NotNull String CLASS_NAME   = "FontData";                                // The name of the generated class
    public static final @NotNull String FILE_PATH    = PACKAGE_PATH + "/" + CLASS_NAME + ".java"; // The path to the generated class
    public static final          int    PARTS = 32;                                               // The number of methods to split the initialization into
    public static final          int    BREAK = 32;                                               // The maximum number of values to place in a single line
    public static final          int    SECTOR_SIZE  = (Character.MAX_VALUE + 1) / PARTS;




    /**
     * Retrieves the width of every character that fits in a Java {@code char} and saves it in the output file.
     * <p>
     * The output file is a Java class that contains the widths and heights of the characters.
     * This is not a ready-made utility class but a container for the raw values.
     */
    public static void generate() {

        // Retrieve text renderer from the client instance and create the generated package
        final Font renderer = Minecraft.getInstance().font;
        final Path dirPath = FabricLoader.getInstance().getConfigDir().resolve(PACKAGE_PATH);
        try {
            Files.createDirectories(dirPath);
        } catch(final IOException e) {
            FrameworkLib.LOGGER.error("Couldn't create configuration directory \"{}\".", dirPath.toString(), e);
        }


        // Print the widths to the source file
        final Path filePath = FabricLoader.getInstance().getConfigDir().resolve(FILE_PATH);
        try(FileWriter f = new FileWriter(filePath.toString())) {


            // Write package, imports, class, fields and static initializer
            f.write(
                "package " + PACKAGE_NAME + ";\n" +
                "import org.jetbrains.annotations.NotNull;\n" +
                "\n\n\n\n" +
                "public final class " + CLASS_NAME + "{\n" +
                "    private " + CLASS_NAME + "() {}\n" +
                "\n"+
                "    // This value identifies the amount of rendered text pixels that fit in a minecraft block\n" +
                "    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;\n" +
                "\n"+
                "    // Font height\n" +
                "    public static final int HEIGHT = " + renderer.lineHeight + ";\n" +
                "\n"+
                "    // The list of widths\n" +
                "    public static final int[] WIDTHS = new int[" + (int)Character.MAX_VALUE + "];\n" +
                "    static {\n"
            );
            for(int i = 0; i < PARTS; ++i) {
                f.write("        init_" + i + "();\n");
            }
            f.write("    }\n");


            // Write initializer methods
            for(int i = 0; i < PARTS; ++i) {
                f.write("\n    private static void init_" + i + "() {\n");
                f.write("        final @NotNull int[] a = new int[] {\n            ");
                for(int c0 = 0; c0 < SECTOR_SIZE; ++c0) {
                    final char c = (char)(SECTOR_SIZE * i + c0);
                    if(c < Character.MAX_VALUE) {
                        f.write(String.format("0x%01x", c < 32 ? 0 : renderer.width(String.valueOf(c))));
                        f.write(",");
                        if(c0 < SECTOR_SIZE - 1) f.write(((c + 1) % BREAK == 0) ? "\n            " : " ");
                    }
                    else break;
                }
                f.write(String.format(
                    "\n        };" +
                    "\n        for(int i = 0; i < %d; ++i) WIDTHS[%d + i] = a[i];\n",
                    SECTOR_SIZE - (i == PARTS - 1 ? 1 : 0),
                    SECTOR_SIZE * i
                ));
                f.write("    }\n");
            }


            f.write("}");
        } catch(final IOException e) {
            FrameworkLib.LOGGER.error("Couldn't write font data file \"{}\".", filePath, e);
        }


        // Print output notice
        FrameworkLib.LOGGER.info("Font data written to \"{}\".", filePath);
    }
}
