package net.abraxator.moresnifferflowers.compat.jei.corruption;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class CorruptionCategory implements IRecipeCategory<CorruptionJEIRecipe> {
    public static final RecipeType<CorruptionJEIRecipe> CORRUPTING = RecipeType.create(MoreSnifferFlowers.MOD_ID, "corrupting", CorruptionJEIRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    
    public CorruptionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(MoreSnifferFlowers.loc("textures/gui/container/corrupting_jei.png"), 0, 0, 120, 40);
        this.icon = helper.createDrawableItemStack(ModItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance());
        this.localizedName = Component.translatableWithFallback("gui.moresnifferflowers.corrupting_category", "Corrupting");
    }

    @Override
    public RecipeType<CorruptionJEIRecipe> getRecipeType() {
        return CORRUPTING;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CorruptionJEIRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 15).addItemStacks(recipe.source());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 15).addItemStack(recipe.corrupted())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipe.corrupted().is(Blocks.AIR.asItem())) {
                        tooltip.add(Component.literal("Air").withStyle(ChatFormatting.WHITE));
                    }
                });
    }

    @Override
    public void draw(CorruptionJEIRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }
}
