package net.abraxator.moresnifferflowers.client.particle;

import net.abraxator.moresnifferflowers.init.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BondripiaParticle extends TextureSheetParticle {
    protected BondripiaParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed) {
                this.xd *= 0.98F;
                this.yd *= 0.98F;
                this.zd *= 0.98F;
            }
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }

    }

    protected void postMoveUpdate() {
    }

    @OnlyIn(Dist.CLIENT)
    static class DripHangParticle extends BondripiaParticle {
        private final ParticleOptions fallingParticle;

        DripHangParticle(ClientLevel level, double x, double y, double z, ParticleOptions fallingParticle) {
            super(level, x, y, z);
            this.fallingParticle = fallingParticle;
            this.gravity *= 0.02F;
            this.lifetime = 40;
        }

        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }

        }

        protected void postMoveUpdate() {
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
        }
    }
    @OnlyIn(Dist.CLIENT)
    static class FallingParticle extends BondripiaParticle {
        FallingParticle(ClientLevel level, double x, double y, double z) {
            this(level, x, y, z, (int)(64.0 / (Math.random() * 0.8 + 0.2)));
        }

        FallingParticle(ClientLevel level, double x, double y, double z, int lifetime) {
            super(level, x, y, z);
            this.lifetime = lifetime;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    static class FallAndLandParticle extends BondripiaParticle.FallingParticle {
        protected final ParticleOptions landParticle;

        FallAndLandParticle(ClientLevel level, double x, double y, double z, ParticleOptions landParticle) {
            super(level, x, y, z);
            this.landParticle = landParticle;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    static class BondripiaSplashParticle extends WaterDropParticle {
        BondripiaSplashParticle(ClientLevel level, double x, double y, double z, double xS, double yS, double zS) {
            super(level, x, y, z);
            this.gravity = 0.01F;
            this.yd = 0.05;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class BondripiaDripProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BondripiaDripProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.DripHangParticle(pLevel, pX, pY, pZ, ModParticles.BONDRIPIA_FALL.get());
            bondripiaParticle.pickSprite(this.spriteSet);
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }
    }

    public static class BondripiaFallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BondripiaFallProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.FallAndLandParticle(pLevel, pX, pY, pZ, ModParticles.BONDRIPIA_LAND.get());
            bondripiaParticle.pickSprite(this.spriteSet);
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }
    }

    public static class BondripiaLandProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BondripiaLandProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            var splashParticle = new BondripiaSplashParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            splashParticle.pickSprite(this.spriteSet);
            return splashParticle;
        }
    }

    public static class AcidripiaDripProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AcidripiaDripProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.DripHangParticle(pLevel, pX, pY, pZ, ModParticles.ACIDRIPIA_FALL.get());
            bondripiaParticle.pickSprite(this.spriteSet);
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }

    }

    public static class AcidripiaFallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AcidripiaFallProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BondripiaParticle bondripiaParticle = new BondripiaParticle.FallAndLandParticle(pLevel, pX, pY, pZ, ModParticles.ACIDRIPIA_LAND.get());
            bondripiaParticle.pickSprite(this.spriteSet);
            bondripiaParticle.lifetime = (int)(16.0 / (Math.random() * 0.4 + 0.2));
            bondripiaParticle.gravity = 0.007F;
            return bondripiaParticle;
        }

    }

    public static class AcidripiaLandProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AcidripiaLandProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            var splashParticle = new BondripiaSplashParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            splashParticle.pickSprite(this.spriteSet);
            return splashParticle;
        }

    }
}
