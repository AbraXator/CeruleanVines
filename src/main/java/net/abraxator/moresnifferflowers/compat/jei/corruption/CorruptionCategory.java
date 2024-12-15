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

public class CorruptionCategory implements IRecipeCategory<CorruptionRecipe> {
    public static final RecipeType<CorruptionRecipe> CORRUPTING = RecipeType.create(MoreSnifferFlowers.MOD_ID, "corrupting", CorruptionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    
    public CorruptionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(MoreSnifferFlowers.loc("textures/gui/container/corrupting_jei.png"), 0, 0, 176, 84);
        this.icon = helper.createDrawableItemStack(ModItems.CORRUPTED_SLIME_BALL.get().getDefaultInstance());
        this.localizedName = Component.translatableWithFallback("gui.moresnifferflowers.corrupting_category", "Corrupting");
    }

    @Override
    public RecipeType<CorruptionRecipe> getRecipeType() {
        return CORRUPTING;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CorruptionRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 34).addItemStack(recipe.source());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 34).addItemStack(recipe.corrupted())
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipe.corrupted().is(Blocks.AIR.asItem())) {
                        tooltip.add(Component.literal("Air").withStyle(ChatFormatting.WHITE));
                    }
                });
    }

    @Override
    public void draw(CorruptionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        String text = "Chance: " + recipe.chance() + "%";
        int width = minecraft.font.width(text);
        int x = getWidth() - 53 - width;
        int y = 23;
        guiGraphics.drawString(minecraft.font, text, x, y, 0x714a5f, false);
    }
}
