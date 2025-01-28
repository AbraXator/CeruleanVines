package net.abraxator.moresnifferflowers.entities.goals;

import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BoblingAttackPlayerGoal extends MeleeAttackGoal {
    private BoblingEntity bobling;
    
    public BoblingAttackPlayerGoal(BoblingEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.bobling = pMob;
    }

    @Override
    public boolean canUse() {
        return !bobling.isRunning() && super.canUse();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (pDistToEnemySqr <= this.getAttackReachSqr(pEnemy) && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            if(this.mob instanceof BoblingEntity bobling && pEnemy instanceof ServerPlayer serverPlayer) {
                ModAdvancementCritters.BOBLING_ATTACK.trigger(serverPlayer);
                bobling.setRunning(true);
            }
        }
    }
}
