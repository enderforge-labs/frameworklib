package com.snek.frameworklib.graphics.basic.elements;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.data_types.displays.CustomItemDisplay;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;
import com.snek.frameworklib.graphics.core.elements.Elm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;








/**
 * An element that can display items.
 * @since v1.1.0
 */
public class ItemElm extends Elm {

    private @NotNull CustomItemDisplay getThisEntity() {
        assert Require.nonNull(getEntity(), "entity");
        assert Require.instanceOf(getEntity(), CustomItemDisplay.class, "entity");
        return getEntity(CustomItemDisplay.class);
    }
    private @NotNull ItemStyle getThisStyle () {
        assert Require.nonNull(getStyle(), "style");
        assert Require.instanceOf(getStyle(), ItemStyle.class, "style");
        return getStyle (ItemStyle.class);
    }





    // Item transform exceptions
    private static final @NotNull Map<
        @NotNull Item,
        @Nullable Pair<
            @NotNull ItemDisplayContext,
            @NotNull Transform
        >
    > transformExceptions = new HashMap<>(Map.ofEntries(
        //TODO
        // Map.entry(Items.TRIDENT, Pair.from(
        //     ItemDisplayContext.GUI,
        //     new Transform()
        // )),
        // Map.entry(Items.SHIELD, Pair.from(
        //     ItemDisplayContext.GROUND,
        //     new Transform().scale(2.5f).moveY(-0.15f).rotY((float)Math.PI)
        // )),


        // Map.entry(Items.LECTERN,        Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        // Map.entry(Items.CARVED_PUMPKIN, Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI)))
    ));




    // Tag transform exceptions
    private static final @NotNull Map<
        @NotNull TagKey<Item>,
        @Nullable Pair<
            @NotNull ItemDisplayContext,
            @NotNull Transform
        >
    > tagTransformExceptions = new HashMap<>(Map.ofEntries(
        //TODO
        // Map.entry(ItemTags.BANNERS, Pair.from(
        //     ItemDisplayContext.NONE,
        //     new Transform().scale(0.6f).moveY(-0.08f).rotY((float)Math.PI)
        // )),
        // Map.entry(ItemTags.BEDS, Pair.from(
        //     ItemDisplayContext.GROUND,
        //     new Transform().scale(2.5f).moveY(-0.14f)
        // )),
        // Map.entry(ItemTags.FENCES, Pair.from(
        //     ItemDisplayContext.GROUND,
        //     new Transform().rotY((float)Math.PI)
        // ))
    ));








    /**
     * Creates a new ItemElm using a custom style.
     * @param level The level in which to place the element.
     * @param style The custom style.
     */
    public ItemElm(final @NotNull ServerLevel level, final @NotNull ItemStyle style) {
        super(level, new CustomItemDisplay(level), style);
    }


    /**
     * Creates a new ItemElm using the default style.
     * @param level The level in which to place the element.
     */
    public ItemElm(final @NotNull ServerLevel level) {
        super(level, new CustomItemDisplay(level), new ItemStyle());
    }




    @Override
    public void flushStyle() {

        // Apply item stack
        { final Flagged<ItemStack> f = getThisStyle().getFlaggedItem();
        if(f.isFlagged()) {
            getThisEntity().setItemStack(f.get());
            f.unflag();
        }}


        // Apply display context
        { final Flagged<ItemDisplayContext> f = getThisStyle().getFlaggedDisplayContext();
        if(f.isFlagged()) {
            getThisEntity().setDisplayType(f.get());
            f.unflag();
        }}


        // Handle transform calculations separately
        {
            final Flagged<Transform> f = getThisStyle().getFlaggedTransform();
            if(f.isFlagged()) {
                final Transform t = __calcTransform();
                t.moveY(t.getScale().y / 2f);
                getThisEntity().setTransformation(t.moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                f.unflag();
            }
        }


        // Handle the other inherited values normally
        super.flushStyle();
    }








    @Override
    public @NotNull Transform __calcTransform() {

        // Retrieve parent transformation and exception. Item exceptions have priority over tag exceptions
        Pair<ItemDisplayContext, Transform> exception = transformExceptions.get(getThisStyle().getItem().getItem());
        if(exception == null) for(final var entry : tagTransformExceptions.entrySet()) {
            if(getThisStyle().getItem().is(entry.getKey())) {
                exception = entry.getValue();
                break;
            }
        }

        // Calculate the parent transform and apply the transform exception if needed
        final Transform t = super.__calcTransform();
        if(exception != null && getThisStyle().getDisplayContext() == exception.getFirst()) {
            t.apply(exception.getSecond());
        }
        return t;
    }




    @Override
    protected void prepareEntityForSpawn(final @NotNull Vector3d pos) {
        // Empty
    }
}