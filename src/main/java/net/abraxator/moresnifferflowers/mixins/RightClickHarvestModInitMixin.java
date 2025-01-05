package net.abraxator.moresnifferflowers.mixins;

import io.github.jamalam360.rightclickharvest.RightClickHarvestModInit;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.init.ModItems;
import net.abraxator.moresnifferflowers.init.ModTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RightClickHarvestModInit.class)
public class RightClickHarvestModInitMixin {
    @Inject(method = "onBlockUse(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;Z)Lnet/minecraft/world/InteractionResult;", at = @At("HEAD"), cancellable = true, remap = false)
    public void moresnifferflowers$onBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult, boolean initialCall, CallbackInfoReturnable<InteractionResult> cir) {
        if(player.getItemInHand(hand).is(ModItems.JAR_OF_BONMEEL.get()) && level.getBlockState(hitResult.getBlockPos()).is(ModTags.ModBlockTags.BONMEELABLE)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}