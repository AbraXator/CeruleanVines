package net.abraxator.moresnifferflowers.init;

import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.PlayerTrigger;

import java.util.Optional;

public class ModAdvancementCritters {
    public static final PlayerTrigger EARN_SNIFFER_ADVANCEMENT = new PlayerTrigger();
    public static final PlayerTrigger USED_DYESPRIA = new PlayerTrigger();
    public static final PlayerTrigger USED_BONMEEL = new PlayerTrigger();
    public static final PlayerTrigger PLACED_DYESPRIA_PLANT = new PlayerTrigger();
    public static final PlayerTrigger BOBLING_ATTACK = new PlayerTrigger();
    public static final PlayerTrigger DYE_BOAT = new PlayerTrigger();

    public static Criterion<?> getSnifferAdvancement() {
        return EARN_SNIFFER_ADVANCEMENT.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<?> usedDyespria() {
        return USED_DYESPRIA.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<?> usedBonmeel() {
        return USED_BONMEEL.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<?> placedDyespriaPlant() {
        return PLACED_DYESPRIA_PLANT.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<?> boblingAttack() {
        return BOBLING_ATTACK.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<?> dyeBoat() {
        return DYE_BOAT.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static void init() {
        CriteriaTriggers.register(MoreSnifferFlowers.sLoc("earn_sniffer_advancement"), EARN_SNIFFER_ADVANCEMENT);
        CriteriaTriggers.register(MoreSnifferFlowers.sLoc("used_dyespria"), USED_DYESPRIA);
        CriteriaTriggers.register(MoreSnifferFlowers.sLoc("used_bonmeel"), USED_BONMEEL);
        CriteriaTriggers.register(MoreSnifferFlowers.sLoc("placed_dyespria_plant"), PLACED_DYESPRIA_PLANT);
        CriteriaTriggers.register(MoreSnifferFlowers.sLoc("bobling_attack"), BOBLING_ATTACK);
        CriteriaTriggers.register(MoreSnifferFlowers.sLoc("dye_boat"), DYE_BOAT);
    }
}

