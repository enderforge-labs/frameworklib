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
import com.snek.frameworklib.graphics.basic.styles.ItemElmStyle;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * An element that can display items.
 */
public class ItemElm extends Elm {

    private @NotNull CustomItemDisplay getThisEntity() {
        assert Require.nonNull(getEntity(), "entity");
        assert Require.instanceOf(getEntity(), CustomItemDisplay.class, "entity");
        return getEntity(CustomItemDisplay.class);
    }
    private @NotNull ItemElmStyle getThisStyle () {
        assert Require.nonNull(getStyle(), "style");
        assert Require.instanceOf(getStyle(), ItemElmStyle.class, "style");
        return getStyle (ItemElmStyle.class);
    }




    // Item transform exceptions
    private static final @NotNull Map<
        @NotNull Item,
        @Nullable Pair<
            @NotNull ItemDisplayContext,
            @NotNull Transform
        >
    > transformExceptions = new HashMap<>(Map.ofEntries(
        Map.entry(Items.TRIDENT, Pair.from(
            ItemDisplayContext.GUI,
            new Transform()
        )),
        Map.entry(Items.SHIELD, Pair.from(
            ItemDisplayContext.GROUND,
            new Transform().scale(2.5f).moveY(-0.15f).rotY((float)Math.PI)
        )),

        Map.entry(Items.PLAYER_HEAD,           Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.PIGLIN_HEAD,           Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.ZOMBIE_HEAD,           Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.DRAGON_HEAD,           Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.CREEPER_HEAD,          Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.SKELETON_SKULL,        Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.WITHER_SKELETON_SKULL, Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),


        Map.entry(Items.CHEST,         Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.TRAPPED_CHEST, Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI))),
        Map.entry(Items.ENDER_CHEST,   Pair.from(ItemDisplayContext.NONE, new Transform().rotY((float)Math.PI)))
    ));




    // Tag transform exceptions
    private static final @NotNull Map<
        @NotNull TagKey<Item>,
        @Nullable Pair<
            @NotNull ItemDisplayContext,
            @NotNull Transform
        >
    > tagTransformExceptions = new HashMap<>(Map.ofEntries(
        Map.entry(ItemTags.BANNERS, Pair.from(
            ItemDisplayContext.NONE,
            new Transform().scale(0.6f).moveY(-0.08f).rotY((float)Math.PI)
        )),
        Map.entry(ItemTags.BEDS, Pair.from(
            ItemDisplayContext.GROUND,
            new Transform().scale(2.5f).moveY(-0.14f)
        ))
    ));








    /**
     * Creates a new ItemElm using a custom style.
     * @param level The level in which to place the element.
     * @param style The custom style.
     */
    protected ItemElm(final @NotNull ServerLevel level, final @NotNull ElmStyle style) {
        super(level, new CustomItemDisplay(level), style);
    }


    /**
     * Creates a new ItemElm using the default style.
     * @param level The level in which to place the element.
     */
    public ItemElm(final @NotNull ServerLevel level) {
        super(level, new CustomItemDisplay(level), new ItemElmStyle());
    }




    @Override
    public void flushStyle() {

        // Apply item stack
        { final Flagged<ItemStack> f = getThisStyle().getFlaggedItem();
        if(f.isFlagged()) {
            getThisEntity().setItemStack(f.get());
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

        // Update the entity's display type and apply the exception's transformation to the parent one if needed
        getThisEntity().setDisplayType(exception == null ? ItemDisplayContext.NONE : exception.getFirst());
        final Transform t = super.__calcTransform();
        return exception == null ? t : t.apply(exception.getSecond());
        //FIXME shield and other y-translated items don't go up enough when the edit animation is triggered
        //FIXME ^ y translation doesn't scale with y size so the final translation looks greater on smaller scales
    }




    @Override
    public void spawn(@NotNull final Vector3d pos, final boolean animate) {
        super.spawn(pos, animate);
    }
    @Override
    protected void prepareEntityForSpawn(final @NotNull Vector3d pos) {
        getThisEntity().setDisplayType(ItemDisplayContext.NONE);
    }
}