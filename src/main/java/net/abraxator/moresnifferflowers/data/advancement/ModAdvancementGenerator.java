package net.abraxator.moresnifferflowers.data.advancement;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
        var root = Advancement.Builder.advancement()
                .display(
                        Items.SNIFFER_EGG.getDefaultInstance(),
                        Component.translatable("advancements.more_sniffer_flowers.any_seed"),
                        Component.translatable("advancements.more_sniffer_flowers.any_seed.desc"),
                        MoreSnifferFlowers.loc("textures/gui/grass_block_bg.png"),
                        AdvancementType.TASK,
                        true,
                        false,
                        false)
                .addCriterion("has_advancement", ModAdvancementCritters.getSnifferAdvancement())
                .save(saver, MoreSnifferFlowers.loc("root").toString());

        var dyespria_plant = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.DYESPRIA.get(),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria_plant"),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria_plant.desc"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("planted_dyespria_plant", ModAdvancementCritters.placedDyespriaPlant())
                .save(saver, MoreSnifferFlowers.loc("dyespria_plant").toString());

        Advancement.Builder.advancement()
                .parent(dyespria_plant)
                .display(
                        ModBlocks.CAULORFLOWER.get(),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria"),
                        Component.translatable("advancements.more_sniffer_flowers.dyespria.desc"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("used_dyespria", ModAdvancementCritters.usedDyespria())
                .save(saver, MoreSnifferFlowers.loc("dyespria").toString());

        var bonmeel = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.JAR_OF_BONMEEL.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bonmeel", "Let It Grow!"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bonmeel.desc", "Enlarge your crops using the magic of Bonmeel"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("used_bonmeel", ModAdvancementCritters.usedBonmeel())
                .save(saver, MoreSnifferFlowers.loc("bonmeel").toString());

        Advancement.Builder.advancement()
                .parent(bonmeel)
                .display(
                        ModItems.CROPRESSED_BEETROOT.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.cropressor", "Compressing with extra steps"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.cropressor.desc", "Cropress any crop"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_cropressed_crop", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.ModItemTags.CROPRESSED_CROPS).build()))
                .save(saver, MoreSnifferFlowers.loc("cropressor").toString());

        Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.REBREWING_STAND.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.rebrew", "Local Rebrewery"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.rebrew.desc", "Rebrew an Extracted Potion"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_rebrewed_potion", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.ModItemTags.REBREWED_POTIONS).build()))
                .save(saver, MoreSnifferFlowers.loc("rebrew").toString());

        var bobling = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModItems.CORRUPTED_BOBLING_CORE.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bobling", "Fight back!"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.bobling.desc", "Fight back the tree madness"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("bobling_attacked", ModAdvancementCritters.boblingAttack())
                .save(saver, MoreSnifferFlowers.loc("bobling").toString());
        
        Advancement.Builder.advancement()
                .parent(bobling)
                .display(
                        ModItems.CORRUPTED_SLIME_BALL.get(),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.corruption", "Evil Blocks"),
                        Component.translatableWithFallback("advancements.more_sniffer_flowers.corruption.desc", "Corrupt blocks around you, to make them evil"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("has_corrupted_slime_ball", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModItems.CORRUPTED_SLIME_BALL).build()))
                .save(saver, MoreSnifferFlowers.loc("corruption").toString());
    }

    private String id(String name) {
        return "%s:%s".formatted(MoreSnifferFlowers.MOD_ID, name);
    }
}
