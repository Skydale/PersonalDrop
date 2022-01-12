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
    @Shadow @Nullable public abstract UUID getOwner();

    @Inject(
            at = @At("HEAD"),
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V",
            cancellable = true
    )
    public void personal_drop_onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (!Objects.equals(getOwner(), player.getUuid())) {
            ci.cancel();
        }
    }
}
