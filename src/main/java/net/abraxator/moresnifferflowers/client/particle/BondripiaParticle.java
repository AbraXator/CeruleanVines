package net.abraxator.moresnifferflowers.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class BondripiaParticle extends DripParticle {
    public BondripiaParticle(ClientLevel pLevel, double pX, double pY, double pZ, Fluid pType) {
        super(pLevel, pX, pY, pZ, pType);
        this.gravity = 0.5F;
        this.lifetime = 40;
        this.setColor(0.6666F, 0.3176F, 0.6980F);
    }

    @Override
    protected void preMoveUpdate() {
        
    }

    @Override
    protected void postMoveUpdate() {
        this.xd *= 0.02;
        this.yd *= 0.02;
        this.zd *= 0.02;
        
        if(this.onGround) {
            this.remove();
        }
    }
    
    public static class BondripiaParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BondripiaParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle(pLevel, pX, pY, pZ, Fluids.WATER);
            bondripiaParticle.pickSprite(spriteSet);
            return bondripiaParticle;
        }
    }
}
