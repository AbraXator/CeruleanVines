package net.abraxator.moresnifferflowers.entities.boat;

import net.abraxator.moresnifferflowers.blocks.ColorableVivicusBlock;
import net.abraxator.moresnifferflowers.components.Dye;
import net.abraxator.moresnifferflowers.init.ModAdvancementCritters;
import net.abraxator.moresnifferflowers.init.ModEntityTypes;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.items.DyespriaItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

public class VivicusBoatEntity extends ModBoatEntity implements ColorableVivicusBlock {
    private static final EntityDataAccessor<Integer> COLOR_DATA = SynchedEntityData.defineId(VivicusBoatEntity.class, EntityDataSerializers.INT);
    
    public VivicusBoatEntity(EntityType<? extends ModBoatEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public VivicusBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(ModEntityTypes.MOD_VIVICUS_BOAT.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR_DATA, DyeColor.WHITE.getId());
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
        if (dyespria.is(ModItems.DYESPRIA.get())) {
            var dye = Dye.getDyeFromDyespria(dyespria);
            int uses = DyespriaItem.getDyespriaUses(dyespria);
            int dyeCount;

            if(uses <= 0) {
                dyeCount = dye.amount() - 1;
                DyespriaItem.setDyespriaUses(dyespria, 4);
            } else {
                dyeCount = dye.amount();
                DyespriaItem.setDyespriaUses(dyespria, uses);
            }

            this.setColor(dye.color());
            var stack = Dye.stackFromDye(new Dye(dye.color(), dyeCount));
            Dye.setDyeToDyeHolderStack(dyespria, stack, stack.getCount());
            
            if(pPlayer instanceof ServerPlayer player) {
                ModAdvancementCritters.DYE_BOAT.trigger(player);
            }
            
            if(this.level().isClientSide) {
                particles(this.random, this.level(), dye, BlockPos.containing(this.position()));
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        return super.interact(pPlayer, pHand);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Color", this.getColor().getId());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setColor(Dye.colorFromId(pCompound.getInt("Color")));
    }
}
