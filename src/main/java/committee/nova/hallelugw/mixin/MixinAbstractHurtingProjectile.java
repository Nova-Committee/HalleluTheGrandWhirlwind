package committee.nova.hallelugw.mixin;

import committee.nova.hallelugw.api.ExtendedBreezeWindCharge;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHurtingProjectile.class)
public abstract class MixinAbstractHurtingProjectile {
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void inject$addAdditionalData(CompoundTag tag, CallbackInfo ci) {
        if (!(this instanceof ExtendedBreezeWindCharge extended)) return;
        tag.putFloat("windcharge_dmg", extended.hallelugw$getDamage());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void inject$readAdditionalData(CompoundTag tag, CallbackInfo ci) {
        if (!(this instanceof ExtendedBreezeWindCharge extended)) return;
        if (tag.contains("windcharge_dmg", 5)) extended.hallelugw$setDamage(tag.getFloat("windcharge_dmg"));
    }
}
