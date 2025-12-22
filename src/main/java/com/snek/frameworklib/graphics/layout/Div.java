package com.snek.frameworklib.graphics.layout;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Hoverable;
import com.snek.frameworklib.graphics.interfaces.Scrollable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
















/**
 * The most basic graphic element. It can contain and manage any amount of elements.
 * <p>
 * By default, divs are invisible and don't exist in the minecraft level nor on the client.
 * They have a 2D size, a 2D position and alignment options.
 */
public class Div {


    // Tree data
    protected @Nullable Canvas canvas = null;
    protected @Nullable Div    parent = null;
    protected final @NotNull List<@NotNull Div> children = new ArrayList<>();


    // Element data
    protected boolean isSpawned = false;
    protected boolean isNew = true;


    // Graphics data
    protected final @NotNull Vector2f localSize = new Vector2f(1, 1);
    protected final @NotNull Vector2f localPos  = new Vector2f(0, 0);
    protected final @NotNull Vector2f absSize   = new Vector2f(1, 1);
    protected final @NotNull Vector2f absPos    = new Vector2f(0, 0);
    protected @NotNull AlignmentX alignmentX = AlignmentX.NONE;
    protected @NotNull AlignmentY alignmentY = AlignmentY.NONE;
    protected int zIndex = 0;




    /**
     * Returns the canvas that contains this element.
     * <p>
     * The canvas of a Canvas is itself.
     * @return The canvas.
     */
    public @Nullable Canvas getCanvas() {
        return canvas;
    }


    /**
     * Sets the reference to the parent canvas of this element and all of its children.
     * <p>
     * This method entirely skips branches whose root already has {@code canvas} as canvas.
     * It assumes all the children of an element share the same canvas.
     * This is not a problem unless the canvas reference is modified externally without changing the children, which is NOT allowed and should never happen.
     * @param canvas The canvas.
     */
    public void setCanvas(final @Nullable Canvas canvas) {
        setCanvasSelf(canvas);
        for(final @NotNull Div c : children) {
            if(c.canvas != canvas) {
                c.setCanvas(canvas);
            }
        }
    }
    private void setCanvasSelf(final @Nullable Canvas canvas) {
        this.canvas = canvas;
    }


    /**
     * Returns the parent of this element.
     * <p>
     * The parent of a Canvas is null.
     * @return
     */
    public @Nullable Div getParent() {
        return parent;
    }


    /**
     * Sets the parent of this object.
     * @param _parent The parent.
    */
    public void setParent(final @Nullable Div parent) {
       this.parent = parent;
    }


    /**
     * Checks if this element is currently spawend.
     * @return True if the element is currently spawned, false otherwise.
     */
    public boolean isSpawned() {
        return isSpawned;
    }


    /**
     * Checks if this element has been spawned at least once.
     * Despawned elements count as not new.
     * @return True if the element has never been spawned before, false otherwise.
     */
    public boolean isNew() {
        return isNew;
    }








    /**
     * Creates a new Div with size (1,1)
     */
    public Div() {
        // Empty
    }



    /**
     * Adds a child to this Div.
     * @param elm The new element.
     * @return {@code elm}.
     */
    public @NotNull Div addChild(final @NotNull Div elm) {
        assert Require.nonNull(elm, "element");

        elm.parent = this;
        elm.setCanvas(canvas);
        elm.updateAbsSize(); //TODO idk if this is needed, though it most likely is
        elm.updateAbsPos();
        elm.updateZIndex();
        children.add(elm);
        return elm;
    }


    /**
     * Removes a child from this Div, without despawning it.
     * @param elm The element to remove.
     * @return {@code elm}.
     */
    public @NotNull Div removeChild(final @NotNull Div elm) {
        assert Require.nonNull(elm, "element");

        elm.parent = null;
        elm.updateAbsSize(); //TODO idk if this is needed, though it most likely is
        elm.updateAbsPos();
        elm.updateZIndex();
        children.remove(elm);
        return elm;
    }


    /**
     * Removes all children from this Div, without despawning them.
     */
    public void clearChildren() {
        for(final Div elm : children) {
            elm.parent = null;
            elm.updateAbsSize(); //TODO idk if this is needed, though it most likely is
            elm.updateAbsPos();
            elm.updateZIndex();
        }
        children.clear();
    }


    /**
     * Returns the list of children.
     * @return The list of children.
     */
    public @NotNull List<@NotNull Div> getChildren() {
        return children;
    }




    /**
     * Applies an animation to this element.
     * <p>
     * Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimation(final @NotNull Animation animation) {
        assert Require.nonNull(animation, "animation");
        // Empty
    }
    /**
     * Applies a transition to this element.
     * <p>
     * Partial steps at the end of the transition are expanded to cover the entire step.
     * @param transition The transition to apply.
     */
    public final void applyAnimation(final @NotNull Transition transition) {
        assert Require.nonNull(transition, "transition");
        applyAnimation(new Animation(transition));
    }




    /**
     * Instantly applies an animation to this element, ignoring transition times and easings.
     * @param animation The animation to apply.
     */
    public void applyAnimationNow(final @NotNull Animation animation) {
        assert Require.nonNull(animation, "animation");
        // Empty
    }
    /**
     * Instantly applies a transition to this element, ignoring transition times and easings.
     * @param transition The transition to apply.
     */
    public final void applyAnimationNow(final @NotNull Transition transition) {
        assert Require.nonNull(transition, "transition");
        applyAnimationNow(new Animation(transition));
    }




    /**
     * Applies an animation to this element and all of its children.
     * <p>
     * Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public final void applyAnimationRecursive(final @NotNull Animation animation) {
        assert Require.nonNull(animation, "animation");
        applyAnimation(animation);
        for(final Div elm : children) {
            elm.applyAnimationRecursive(animation);
        }
    }
    /**
     * Applies a transition to this element and all of its children.
     * <p>
     * Partial steps at the end of the transition are expanded to cover the entire step.
     * @param transition The transition to apply.
     */
    public final void applyAnimationRecursive(final @NotNull Transition transition) {
        assert Require.nonNull(transition, "transition");
        applyAnimationRecursive(new Animation(transition));
    }




    /**
     * Instantly applies an animation to this element and all of its children, ignoring transition times and easings.
     * @param animation The animation to apply.
     */
    public final void applyAnimationNowRecursive(final @NotNull Animation animation) {
        assert Require.nonNull(animation, "animation");
        applyAnimationNow(animation);
        for(final Div elm : children) {
            elm.applyAnimationNowRecursive(animation);
        }
    }
    /**
     * Instantly applies a transition to this element and all of its children, ignoring transition times and easings.
     * @param transition The transition to apply.
     */
    public final void applyAnimationNowRecursive(final @NotNull Transition transition) {
        assert Require.nonNull(transition, "transition");
        applyAnimationNowRecursive(new Animation(transition));
    }




    /**
     * Forwards a click to this element and all its children.
     * <p>
     * This method stops at the first clickable element that consumes a click.
     * @param player The player that clicked.
     * @param clickType The type of click.
     * @return Whether the click was accepted by this element.
     */
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction clickType) {
        assert Require.nonNull(player, "player");
        assert Require.nonNull(clickType, "click type");

        // If this element accepts the click, run callback and return
        if(this instanceof Clickable e) {
            final Vector2f coords = e.attemptClick(player, clickType);
            if(coords != null) {
                e.onClick(player, clickType, coords);
                return true;
            }
        }

        //TODO check if this is actually needed. it prob was here to stop clicks from removing elements while iterating the tree
        // If not, send the click event to all children until one of them accepts it
        final List<Div> _children = new ArrayList<>(children);
        for(final Div elm : _children) {
        // for(final Div elm : children) { //TODO
            if(elm.forwardClick(player, clickType)) {
                return true;
            }
        }

        // Return false if no element accepted the click
        return false;
    }




    /**
     * Finds the element that the player is looking at.
     * <p>
     * This method skips non-Elm elements and elements that are not hoverable, not clickable and not scrollable.
     * @param player The player.
     * @return The element being looked at, or null if the player isnt looking at any of the elements.
     */
    public @Nullable Elm findTargetedElement(final @NotNull Player player) {
        assert Require.nonNull(player, "player");

        if((this instanceof Hoverable || this instanceof Clickable || this instanceof Scrollable) && this instanceof Elm e) {
            if(e.checkIntersection(player, false) != null) {
                final Elm targetedChild = findTargetedChild(player);
                if(targetedChild != null) {
                    return targetedChild;
                }
                else {
                    return e;
                }
            }
            else {
                return null;
            }
        }
        else {
            return findTargetedChild(player);
        }
    }


    /**
     * Finds the child element that the player is looking at.
     * <p>
     * This method skips non-Elm elements and elements that are not hoverable, not clickable and not scrollable.
     * @param player The player.
     * @return The element being looked at, or null if the player isnt looking at any of the elements.
     */
    public @Nullable Elm findTargetedChild(final @NotNull Player player) {
        assert Require.nonNull(player, "player");

        for(final Div elm : children) {
            final Elm r = elm.findTargetedElement(player);
            if(r != null) return r;
        }
        return null;
    }




    /**
     * Spawns the element and its associated entities into the level.
     * @param pos The position of the spawned entities.
     * @param animate Whether to animate the element.
     *     Using {@code animate = false} will still apply the primer and spawning animations,
     *     but their effects will be instantly visible instead of appearing gradually over time.
     */
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        assert Require.nonNull(pos, "position");

        if(!isSpawned) {
            for(final Div elm : children) {
                elm.spawn(pos, animate);
            }
            isSpawned = true;
            isNew = false;
        }
    }




    /**
     * Removes the element and its associated entities from the level.
     * @param animate Whether to display despawn animations. Using {@code animate=false} will despawn the entities immediately.
     */
    public void despawn(final boolean animate) {
        if(isSpawned) {
            for(final Div elm : children) {
                elm.despawn(animate);
            }
            isSpawned = false;
        }
    }








    /**
     * Updates the absolute size of this element
     * using the parent's absolute size and the element's local size.
     */
    public void updateAbsSizeSelf() {
        absSize.set(parent == null ? localSize : new Vector2f(parent.getAbsSize()).mul(localSize));
        updateAbsPosSelf();
    }
    /**
     * Updates the local size of this element based on the current absolute size.
     */
    public void updateAbsSizeInverseSelf() {
        localSize.set(parent == null ? absSize : new Vector2f(absSize).div(parent.getAbsSize()));
        updateAbsPosSelf();
    }


    /**
     * Updates the absolute size of this element and its children, recursively,
     * using the parent's absolute size and the element's local size.
     */
    public void updateAbsSize() {
        updateAbsSizeSelf();
        for(final Div c : children) c.updateAbsSize();
    }
    /**
     * Updates the local size of this element based on the current absolute size, then updates the absolute size of its children, recursively.
     */
    public void updateAbsSizeInverse() {
        updateAbsSizeInverseSelf();
        for(final Div c : children) c.updateAbsSize();
    }




    /**
     * Updates the Z-index value of this element
     * using the parent's Z-index and Z-layer count.
     */
    public void updateZIndexSelf() {
        zIndex = parent == null ? 0 : parent.zIndex + parent.getLayerCount();
    }

    /**
     * Updates the Z-index value of this element and its children, recursively,
     * using the parent's Z-index and Z-layer count.
     */
    public void updateZIndex() {
        updateZIndexSelf();
        for(final Div c : children) c.updateZIndex();
    }

    /**
     * Returns the Z-index of this element.
     * @return The Z-index.
     */
    public int getZIndex() {
        return zIndex;
    }

    /**
     * Returns the amount of Z-Layers occupied by this element.
     * @return The amount of Z-Layers.
     */
    public int getLayerCount() {
        return 0;
    }




    /**
     * Updates the absolute position of this element
     * using the parent's absolute position and the element's local position and alignment.
     */
    public void updateAbsPosSelf() {

        // Calculate unrestricted position
        final Vector2f p = parent == null ? new Vector2f(0, 0) : parent.getAbsPos();
        final Vector2f s = parent == null ? new Vector2f(1, 1) : parent.getAbsSize();

        // Apply horizontal alignment
        final float x = switch(alignmentX) {
            case LEFT   -> p.x - (s.x - absSize.x) / 2;
            case RIGHT  -> p.x + (s.x - absSize.x) / 2;
            case CENTER -> p.x;
            case NONE   -> p.x + localPos.x;
        };

        // Apply vertical alignment
        final float y = switch(alignmentY) {
            case TOP    -> p.y + (s.y - absSize.y);
            case BOTTOM -> p.y;
            case CENTER -> p.y + (s.y - absSize.y) / 2;
            case NONE   -> p.y + localPos.y;
        };

        // Update the value
        absPos.set(x, y);
    }


    /**
     * Updates the absolute position of this element and its children, recursively,
     * using the parent's absolute position and the element's local position and alignment.
     */
    public void updateAbsPos() {
        updateAbsPosSelf();
        for(Div c : children) c.updateAbsPos();
    }






    // Setters

    public void setSize(final @NotNull Vector2f size) {
        assert Require.nonNull(size, "size");
        localSize.set(size);
        updateAbsSize();
    }
    public void setAbsSize(final @NotNull Vector2f size) {
        assert Require.nonNull(size, "size");
        absSize.set(size);
        updateAbsSizeInverse();
    }
    public void scale(final @NotNull Vector2f size) {
        assert Require.nonNull(size, "size");
        localSize.mul(size);
        updateAbsSize();
    }
    public void setPos(final @NotNull Vector2f pos) {
        assert Require.nonNull(pos, "position");
        localPos.set(pos );
        updateAbsPos();
    }
    public void move(final @NotNull Vector2f pos) {
        assert Require.nonNull(pos, "position");
        localPos.add(pos );
        updateAbsPos();
    }


    public void setSizeX   (final float x) { localSize.x  = x; updateAbsSize       (); }
    public void setSizeY   (final float y) { localSize.y  = y; updateAbsSize       (); }
    public void setAbsSizeX(final float x) { absSize  .x  = x; updateAbsSizeInverse(); }
    public void setAbsSizeY(final float y) { absSize  .y  = y; updateAbsSizeInverse(); }
    public void scaleX     (final float x) { localSize.x *= x; updateAbsSize       (); }
    public void scaleY     (final float y) { localSize.y *= y; updateAbsSize       (); }
    public void setPosX    (final float x) { localPos .x  = x; updateAbsPos        (); }
    public void setPosY    (final float y) { localPos .y  = y; updateAbsPos        (); }
    public void moveX      (final float x) { localPos .x += x; updateAbsPos        (); }
    public void moveY      (final float y) { localPos .y += y; updateAbsPos        (); }


    public void setAlignmentX(final @NotNull AlignmentX alignmentX) {
        assert Require.nonNull(alignmentX, "x alignment");
        this.alignmentX = alignmentX;
        updateAbsPos();
    }

    public void setAlignmentY(final @NotNull AlignmentY alignmentY) {
        assert Require.nonNull(alignmentY, "y alignment");
        this.alignmentY = alignmentY;
        updateAbsPos();
    }

    public void setAlignment(final @NotNull AlignmentX alignmentX, final @NotNull AlignmentY alignmentY) {
        assert Require.nonNull(alignmentX, "x alignment");
        assert Require.nonNull(alignmentY, "y alignment");
        this.alignmentX = alignmentX;
        this.alignmentY = alignmentY;
        updateAbsPos();
    }




    // Getters
    public @NotNull Vector2f   getLocalSize () { return localSize;  }
    public @NotNull Vector2f   getLocalPos  () { return localPos;   }
    public @NotNull Vector2f   getAbsSize   () { return absSize;  }
    public @NotNull Vector2f   getAbsPos    () { return absPos;   }
    public @NotNull AlignmentX getAlignmentX() { return alignmentX; }
    public @NotNull AlignmentY getAlignmentY() { return alignmentY; }
}
