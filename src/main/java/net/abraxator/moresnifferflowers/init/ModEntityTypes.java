package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.entities.*;
import net.abraxator.moresnifferflowers.entities.boat.ModBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.ModChestBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusBoatEntity;
import net.abraxator.moresnifferflowers.entities.boat.VivicusChestBoatEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MoreSnifferFlowers.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<BoblingEntity>> BOBLING = buildNoEgg(MoreSnifferFlowers.loc("bobling"), makeBuilder(BoblingEntity::new, MobCategory.MISC, 0.375F, 0.8125F, 80, 3), false);
    public static final DeferredHolder<EntityType<?>, EntityType<DragonflyProjectile>> DRAGONFLY = buildNoEgg(MoreSnifferFlowers.loc("dragonfly"), makeBuilder(DragonflyProjectile::new, MobCategory.MISC, 0.21875F, 0.21875F, 80, 3), false);
    public static final DeferredHolder<EntityType<?>, EntityType<CorruptedProjectile>> CORRUPTED_SLIME_BALL = buildNoEgg(MoreSnifferFlowers.loc("corrupted_slime_ball"), makeBuilder(CorruptedProjectile::new, MobCategory.MISC, 0.25F, 0.25F, 80, 3), false);
    public static final DeferredHolder<EntityType<?>, EntityType<ModBoatEntity>> MOD_CORRUPTED_BOAT = buildNoEgg(MoreSnifferFlowers.loc("mod_corrupted_boat"), makeBuilder(ModBoatEntity::new, MobCategory.MISC, 1.375f, 0.5625f, 80, 3), false);
    public static final DeferredHolder<EntityType<?>, EntityType<ModChestBoatEntity>> MOD_CORRUPTED_CHEST_BOAT = buildNoEgg(MoreSnifferFlowers.loc("mod_corrupted_chest_boat"), makeBuilder(ModChestBoatEntity::new, MobCategory.MISC, 1.375f, 0.5625f, 80, 3), false);
    public static final DeferredHolder<EntityType<?>, EntityType<VivicusBoatEntity>> MOD_VIVICUS_BOAT = buildNoEgg(MoreSnifferFlowers.loc("mod_vivicus_boat"), makeBuilder(VivicusBoatEntity::new, MobCategory.MISC, 1.375f, 0.5625f, 80, 3), false);
    public static final DeferredHolder<EntityType<?>, EntityType<VivicusChestBoatEntity>> MOD_VIVICUS_CHEST_BOAT = buildNoEgg(MoreSnifferFlowers.loc("mod_vivicus_chest_boat"), makeBuilder(VivicusChestBoatEntity::new, MobCategory.MISC, 1.375f, 0.5625f, 80, 3), false);
    public static final DeferredHolder<EntityType<?>, EntityType<JarOfAcidProjectile>> JAR_OF_ACID = buildNoEgg(MoreSnifferFlowers.loc("jar_of_acid"), makeBuilder(JarOfAcidProjectile::new, MobCategory.MISC, 0.25F, 0.25F, 80, 3), false);

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, int primary, int secondary) {
        return make(id, factory, classification, width, height, false, primary, secondary);
    }

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> make(ResourceLocation id, EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, boolean fireproof, int primary, int secondary) {
        return build(id, makeBuilder(factory, classification, width, height, 80, 3), fireproof, primary, secondary);
    }

    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> buildNoEgg(ResourceLocation id, EntityType.Builder<E> builder, boolean fireproof) {
        if (fireproof) builder.fireImmune();
        return ENTITIES.register(id.getPath(), () -> builder.build(id.toString()));
    }

    @SuppressWarnings("unchecked")
    private static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> build(ResourceLocation id, EntityType.Builder<E> builder, boolean fireproof, int primary, int secondary) {
        if (fireproof) builder.fireImmune();
        DeferredHolder<EntityType<?>, EntityType<E>> ret = ENTITIES.register(id.getPath(), () -> builder.build(id.toString()));
        if (primary != 0 && secondary != 0) {
            ModItems.ITEMS.register(id.getPath() + "_spawn_egg", () -> new SpawnEggItem((EntityType<? extends Mob>) ret.get(), primary, secondary, new Item.Properties()));
        }

        return ret;
    }

    private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(@SuppressWarnings("unused") Class<E> cast, EntityType.EntityFactory<E> factory, float width, float height, int range, int interval) {
        return makeBuilder(factory, MobCategory.MISC, width, height, range, interval);
    }

    private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.EntityFactory<E> factory, MobCategory classification, float width, float height, int range, int interval) {
        return EntityType.Builder.of(factory, classification).
                sized(width, height).
                setTrackingRange(range).
                setUpdateInterval(interval).
                setShouldReceiveVelocityUpdates(true);
    }
}
