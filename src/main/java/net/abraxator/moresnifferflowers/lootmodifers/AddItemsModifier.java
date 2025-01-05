package net.abraxator.moresnifferflowers.lootmodifers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.abego.treelayout.internal.util.java.util.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AddItemsModifier extends LootModifier {
    public static final Supplier<Codec<AddItemsModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ExtraCodecs.nonEmptyList(ForgeRegistries.ITEMS.getCodec().listOf())
            .fieldOf("item").forGetter(m -> m.items)).apply(inst, AddItemsModifier::new)));
    public static final List<ResourceLocation> SNIFFERENT_ITEMS_LOC = List.of(snifferentLoc("spindlefern_seeds"), snifferentLoc("spineflower_seeds"), snifferentLoc("lumibulb_seeds"), snifferentLoc("sniffberry_seedling"), snifferentLoc("bloom_plant_nut"), snifferentLoc("globar_sapling"), snifferentLoc("club_moss_patch"), snifferentLoc("amber"));
    public static final List<ResourceLocation> HELLIONS_ITEMS_LOC = List.of(hellionsLoc("stone_pine_sapling"), hellionsLoc("fiddlefern"), hellionsLoc("ivy"));
    public static final List<ResourceLocation> QUARK_ITEMS_LOC = List.of(new ResourceLocation("quark", "ancient_sapling"));

    private final List<Item> items;

    public AddItemsModifier(LootItemCondition[] conditionsIn, List<Item> items) {
        super(conditionsIn);
        this.items = items;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();

        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        generatedLoot.clear();
        generatedLoot.add(Items.PITCHER_POD.getDefaultInstance());
        generatedLoot.add(Items.TORCHFLOWER_SEEDS.getDefaultInstance());
        modSupport(SNIFFERENT_ITEMS_LOC, generatedLoot);
        modSupport(HELLIONS_ITEMS_LOC, generatedLoot);
        modSupport(QUARK_ITEMS_LOC, generatedLoot);

        items.forEach(item -> generatedLoot.add(item.getDefaultInstance()));
        newLoot.add(Util.getRandom(generatedLoot, context.getRandom()));
        return newLoot;
    }

    private void modSupport(List<ResourceLocation> itemsLocList, ObjectArrayList<ItemStack> generatedLoot) {
        List<ItemStack> itemList = new ArrayList<>();

        itemsLocList.forEach(resourceLocation -> {
            var item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            if(item != null && !item.getDefaultInstance().is(Items.AIR)) {
                itemList.add(item.getDefaultInstance());
            }
        });

        if(itemList.isEmpty()) {
            return;
        }

        generatedLoot.addAll(itemList);
    }

    private static ResourceLocation snifferentLoc(String path) {
        return new ResourceLocation("snifferent", path);
    }

    private static ResourceLocation hellionsLoc(String path) {
        return new ResourceLocation("snifferplus", path);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}