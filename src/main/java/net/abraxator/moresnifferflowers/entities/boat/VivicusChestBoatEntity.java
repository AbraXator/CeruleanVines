package net.abraxator.moresnifferflowers.entities.boat;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

public class VivicusChestBoatEntity extends ModChestBoatEntity implements ColorableVivicusBlock {
    private static final EntityDataAccessor<Integer> COLOR_DATA = SynchedEntityData.defineId(VivicusChestBoatEntity.class, EntityDataSerializers.INT);
    
    public VivicusChestBoatEntity(EntityType<? extends ModChestBoatEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public VivicusChestBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(ModEntityTypes.MOD_VIVICUS_CHEST_BOAT.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR_DATA, DyeColor.WHITE.getId());
    }
    
    public void setColor(DyeColor color) {
        this.entityData.set(COLOR_DATA, color.getId());
    }

    public DyeColor getColor() {
        return Dye.colorFromId(this.entityData.get(COLOR_DATA));
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        var dyespria = pPlayer.getMainHandItem();
        if (dyespria.is(ModItems.DYESPRIA)) {
            var dye = Dye.getDyeFromStack(dyespria);
            this.setColor(dye.color());
            var stack = Dye.stackFromDye(new Dye(dye.color(), dye.amount() - 1));
            Dye.setDyeToDyeHolderStack(dyespria, stack, stack.getCount());
            
            if(!this.level().isClientSide) {
                particles(this.random, ((ServerLevel) this.level()), dye, BlockPos.containing(this.position()));
            }
            
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        
        return super.interact(pPlayer, pHand);
    }
}
