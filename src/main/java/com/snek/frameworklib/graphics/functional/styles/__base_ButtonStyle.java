package com.snek.frameworklib.graphics.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.utils.UtilityClassBase;




/**
 * A class that contains static data shared by {@link TextButtonStyle} and {@link ButtonStyle}.
 */
public final class __base_ButtonStyle extends UtilityClassBase {
    public static final @NotNull Vector3i HOVER_COLOR          = new Vector3i(220, 220, 220);
    public static final          float    HIDDEN_W             = 0.00001f;
    public static final          int      HOVER_ANIMATION_TIME = 10;
    public static final          int      DEFAULT_BG_ALPHA     = 255;
}