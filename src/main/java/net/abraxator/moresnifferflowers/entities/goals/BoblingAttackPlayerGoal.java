package net.abraxator.moresnifferflowers.entities.goals;

import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BoblingAttackPlayerGoal extends MeleeAttackGoal {
    public BoblingAttackPlayerGoal(BoblingEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pTarget) {
        if (this.canPerformAttack(pTarget)) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pTarget);
            if(this.mob instanceof BoblingEntity bobling && pTarget instanceof ServerPlayer serverPlayer) {
                ModAdvancementCritters.BOBLING_ATTACK.trigger(serverPlayer);
                bobling.setRunning(true);
            }
        }
    }
}
