package com.snek.frameworklib.mixin.enhanced_recipes.shaped;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.enhanced_recipes.shaped.EnhancedShapedRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;




/**
 * A mixin for FrameworkLib's {@link EnhancedShapedRecipe}.
 * <p>
 * By default, Minecraft Vanilla sends all registered recipes to the client.
 * When the client receives a recipe it doesn't recognize, it disconnects immediately.
 * <p>
 * This mixin modifies the list of recipes passed to {@link ClientboundUpdateRecipesPacket}'s constructor,
 * applying any specified client overrides and stripping custom parameters to allow the Vanilla client to read the recipe properly.
 * It also sets the count of the ingredient item stacks to the amount specified in the recipe, as Vanilla only supports single-item ingredient stacks.
 */
@SuppressWarnings("java:S1118") //! Add private constructor to hide the public implicit one
@Mixin(ClientboundUpdateRecipesPacket.class)
public class RecipePacketProxyMixin {


    @ModifyVariable(
        method = "<init>",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private static Collection<Recipe<?>> filterCustomRecipes(final Collection<Recipe<?>> recipes) {
        final List<Recipe<?>> r = new ArrayList<>();
        for(final var recipe : recipes) {


            // If the recipe is an EnhancedShapedRecipe
            if(recipe.getSerializer() == FrameworkLib.ENHANCED_SHAPED_RECIPE_SERIALIZER) {
                final EnhancedShapedRecipe enhancedRecipe = (EnhancedShapedRecipe)recipe;


                // Create a list of modified ingredients. For each ingredient in the recipe
                final NonNullList<Ingredient> modifiedIngredients = NonNullList.createWithCapacity(enhancedRecipe.getIngredients().size());
                for(int i = 0; i < enhancedRecipe.getIngredients().size(); ++i) {
                    final var enhancedIngredient = enhancedRecipe.getIngredients().get(i);
                    final List<ItemStack> modifiedStacks = new ArrayList<>();


                    // If the ingredient specifies a client override
                    final String clientOverride = enhancedRecipe.getClientOverrides().get(i);
                    if(clientOverride != null) {

                        // If the override exists, use it as the stack to display (appears as a normal, single item stack)
                        final @Nullable ItemStack overrideStack = EnhancedShapedRecipe.getItemStackReference(new ResourceLocation(clientOverride));
                        if(overrideStack != null) modifiedStacks.add(overrideStack);

                        // If the override doesn't exist, print an error and use the vanilla ingredient (likely an EMPTY)
                        else FrameworkLib.LOGGER.error("Unknown ItemStack reference: \"{}\"", clientOverride, new RuntimeException());
                    }


                    // If the ingredient doesn't use an override, replace its items with modified item stacks with the correct count
                    else for(int j = 0; j < enhancedIngredient.getItems().length; ++j) {
                        final ItemStack ingredientItem = enhancedIngredient.getItems()[j];
                        modifiedStacks.add(ingredientItem.copyWithCount(enhancedRecipe.getRequiredCounts().get(i)));
                    }


                    // Store the modified item stacks in this modified ingredient
                    modifiedIngredients.add(Ingredient.of(modifiedStacks.stream()));
                }


                // Create a new Vanilla recipe with the modified ingredients and the specified result
                r.add(new ShapedRecipe(
                    enhancedRecipe.getId(),
                    enhancedRecipe.getGroup(),
                    CraftingBookCategory.MISC, //FIXME
                    enhancedRecipe.getWidth(),
                    enhancedRecipe.getHeight(),
                    modifiedIngredients,
                    enhancedRecipe.getResultItem(FrameworkLib.getServer().getAllLevels().iterator().next().registryAccess())
                ));
            }


            // Add other recipes back
            else {
                r.add(recipe);
            }
        }


        return r;
    }
}