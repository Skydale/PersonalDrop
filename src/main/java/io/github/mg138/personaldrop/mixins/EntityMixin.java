package io.github.mg138.personaldrop.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(
            at = @At("RETURN"),
            method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;"
    )
    public void personal_drop_dropStack(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
        final Entity entity = (Entity) (Object) this;

        final ItemEntity itemEntity = cir.getReturnValue();
        if (itemEntity == null) return;

        if (entity instanceof LivingEntity){
            final LivingEntity attacker = ((LivingEntity) entity).getAttacker();

            if (attacker != null) {
                itemEntity.setOwner(attacker.getUuid());
            }
        }
    }
}