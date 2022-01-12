package io.github.mg138.personaldrop.mixins;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow @Nullable public abstract UUID getThrower();

    @Shadow public abstract void setOwner(@Nullable UUID uuid);

    @Shadow @Nullable public abstract UUID getOwner();

    @Inject(
            at = @At("HEAD"),
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V"
    )
    public void personal_drop_onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (getOwner() == null) {
            final UUID thrower = getThrower();

            if (thrower != null) {
                setOwner(thrower);
            }
        }
    }
}
