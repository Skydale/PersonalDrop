package io.github.mg138.personaldrop.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    private static void dropStack(World world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack) {
    }

    private static void dropStack(World world, BlockPos pos, ItemStack stack, Entity entity) {
        float f = EntityType.ITEM.getHeight() / 2.0f;
        double d = (pos.getX() + 0.5f) + MathHelper.nextDouble(world.random, -0.25, 0.25);
        double e = (pos.getY() + 0.5f) + MathHelper.nextDouble(world.random, -0.25, 0.25) - f;
        double g = (pos.getZ() + 0.5f) + MathHelper.nextDouble(world.random, -0.25, 0.25);

        dropStack(world, () -> {
            ItemEntity itemEntity = new ItemEntity(world, d, e, g, stack);

            itemEntity.setOwner(entity.getUuid());

            return itemEntity;
        }, stack);
    }

    @Inject(
            at = @At("HEAD"),
            method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
            cancellable = true
    )
    private static void personal_drop_dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
        if (world instanceof ServerWorld) {
            Block.getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, entity, stack)
                    .forEach(s -> dropStack(world, pos, s, entity));

            state.onStacksDropped((ServerWorld) world, pos, stack);
        }
        ci.cancel();
    }
}
