package net.abraxator.moresnifferflowers.entities.goals;

import net.abraxator.moresnifferflowers.entities.BoblingEntity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class BoblingAvoidPlayerGoal<T extends Player> extends AvoidEntityGoal<T> {
    private final BoblingEntity bobling;
    
    public BoblingAvoidPlayerGoal(BoblingEntity pMob, Class<T> pEntityClassToAvoid, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier) {
        super(pMob, pEntityClassToAvoid, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
        this.bobling = pMob;
    }

    @Override
    public boolean canUse() {
        return this.bobling.isRunning() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.bobling.isRunning() && super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        if(this.bobling.getRandom().nextDouble() <= 1D) this.bobling.setWantedPos(Optional.of(this.path.getEndNode().asBlockPos()));
    }
}
