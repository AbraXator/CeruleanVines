package net.abraxator.moresnifferflowers.items;

import com.google.common.collect.Maps;
import net.abraxator.moresnifferflowers.init.ModBlocks;
import net.abraxator.moresnifferflowers.init.ModParticles;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.AxisAngle4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.rmi.MarshalledObject;
import java.time.Year;
import java.util.*;

public class TrimMaterialItem extends Item {
    public TrimMaterialItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatableWithFallback("tooltip.trim_material_item.usage", "Can be used as an armor trim material").withStyle(ChatFormatting.GOLD));
    }

    public InteractionResult useOn(UseOnContext pContext) {
        var pos = pContext.getClickedPos().getCenter();
        var r = 5;
        float angle = Mth.PI / 4;

        Map<Integer, BlockState> map = Util.make(Maps.newLinkedHashMap(), o -> {
            o.put(0, Blocks.WHITE_WOOL.defaultBlockState());
            o.put(1, Blocks.GRAY_WOOL.defaultBlockState());
            o.put(2, Blocks.BROWN_WOOL.defaultBlockState());
            o.put(3, Blocks.GREEN_WOOL.defaultBlockState());
            o.put(4, Blocks.YELLOW_WOOL.defaultBlockState());
            o.put(5, Blocks.RED_WOOL.defaultBlockState());
        });
        
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if(!shouldRemoveBlock(x, y, z, r)) {
                        Vector3d vec3 = new Vector3d(x, y, z);
                        //vec3.rotateX(angle);
                        //vec3.rotateZ(angle);
                        //vec3.rotateY(angle);
                        pContext.getLevel().setBlock(new BlockPos(
                                        (int) (pos.x + vec3.x),
                                        (int) (pos.y + vec3.y),
                                        (int) (pos.z + vec3.z)),
                                map.get(Math.min(Math.min(Mth.abs(x), Mth.abs(y)), Mth.abs(z))), 3);
                    }
                }
            }
        }

        return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide);
    }

    private boolean isOnEdge(int x, int y, int z, int r) {
        int absX = Mth.abs(x);
        int absY = Mth.abs(y);
        int absZ = Mth.abs(z);

        return  
                absX != r && absY == r && absZ != r ||
                absX == r && absY != r && absZ != r ||
                absX != r && absY != r && absZ == r;
    }

    private boolean shouldRemoveBlock(int x, int y, int z, int r) {
        if(y == 0) {
            return Mth.abs(x) == r && Mth.abs(z) == r;
        } else if(Mth.abs(y) == 1) {
            return Mth.abs(x) == r || Mth.abs(z) == r;
        }

        if(x == 0) {
            return Mth.abs(y) == r && Mth.abs(z) == r;
        } else if(Mth.abs(x) == 1) {
            return Mth.abs(y) == r || Mth.abs(z) == r;
        }

        if(z == 0) {
            return Mth.abs(y) == r && Mth.abs(x) == r;
        } else if(Mth.abs(z) == 1) {
            return Mth.abs(y) == r || Mth.abs(x) == r;
        }
        
        return false;
    }

    private void generateParticle(UseOnContext pContext, Set<Vec3> set, double xo, double yo, double zo, double r, double theta, double checkR) {
        var x = xo + r * Mth.cos((float) theta);
        var yx = yo + r * Mth.sin((float) theta);
        var yz = yo + r * Mth.cos((float) theta);
        var z = zo + r * Mth.sin((float) theta);

        createAndAddParticle(pContext, set, checkR, new Vec3(x, yo, z));
        createAndAddParticle(pContext, set, checkR, new Vec3(x, yx, zo));
        createAndAddParticle(pContext, set, checkR, new Vec3(xo, yz, z));
    }

    private void createAndAddParticle(UseOnContext pContext, Set<Vec3> set, double checkR, Vec3 vec3) {
        AABB aabb = AABB.ofSize(vec3, checkR, checkR, checkR);
        if (set.stream().noneMatch(aabb::contains)) {
            pContext.getLevel().addParticle(ModParticles.CARROT.get(), vec3.x, vec3.y, vec3.z, 0, 0, 0);
            set.add(vec3);
        }
    }
}
