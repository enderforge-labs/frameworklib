package com.snek.frameworklib.mixin.enhanced_recipes.shaped;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.enhanced_recipes.shaped.EnhancedShapedRecipe;
import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;




/**
 * A mixin for FrameworkLib's {@link EnhancedShapedRecipe}.
 * <p>
 * By default, Minecraft Vanilla sends all registered recipes to the client.
 * When the client receives a recipe it doesn't recognize, it disconnects instantly.
 * <p>
 * This mixin modifies the list of recipes passed to {@link ClientboundUpdateRecipesPacket}'s constructor, //TODO update documentation
 * filtering out any {@link EnhancedShapedRecipe} recipes.
 * This stops their packets from ever being sent to the client.
 */
@SuppressWarnings("java:S1118") //! Add private constructor to hide the public implicit one
@Mixin(ClientboundUpdateRecipesPacket.class)
public class RecipePacketSuppressorMixin {


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
                System.out.println("Recipe " + enhancedRecipe.getId().toString());
                System.out.println("");
                for(int i = 0; i < enhancedRecipe.getIngredients().size(); ++i) {
                    final var enhancedIngredient = enhancedRecipe.getIngredients().get(i);

                    // Create a list of modified item stacks. For each item stack in the ingredient
                    final List<ItemStack> modifiedStacks = new ArrayList<>();
                    System.out.println("Slot " + i);
                    for(int j = 0; j < enhancedIngredient.getItems().length; ++j) {
                        final var ingredientItem = enhancedIngredient.getItems()[j];

                        // Create a modified item stack with the correct count, then add it to the list of item stacks
                        modifiedStacks.add(ingredientItem.copyWithCount(enhancedRecipe.getRequiredCounts().get(i)));
                        System.out.println("Added " + enhancedRecipe.getRequiredCounts().get(i) + "x " + MinecraftUtils.getItemName(ingredientItem).getString());
                    }
                    if(enhancedRecipe.getId().toString().equals("fancyplayershops:product_display_t5") && i == 4) {
                        System.out.println("Additional data:");
                        System.out.println("    Amount of items: " + enhancedIngredient.getItems().length);
                        System.out.println("    JSON: " + enhancedIngredient.toJson().toString());
                    }
                    System.out.println("");

                    // Use the modified item stacks as ingredients
                    modifiedIngredients.add(Ingredient.of(modifiedStacks.stream()));
                }
                System.out.println("-------------------------------");


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