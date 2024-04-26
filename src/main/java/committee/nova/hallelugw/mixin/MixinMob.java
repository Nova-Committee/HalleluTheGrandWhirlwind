package committee.nova.hallelugw.mixin;

import committee.nova.hallelugw.api.ExtendedBreeze;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MixinMob {
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void inject$addAdditionalData(CompoundTag tag, CallbackInfo ci) {
        if (!(this instanceof ExtendedBreeze breeze)) return;
        tag.putInt("breeze_size", breeze.hallelugw$getSize());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void inject$readAdditionalData(CompoundTag tag, CallbackInfo ci) {
        if (!(this instanceof ExtendedBreeze breeze)) return;
        if (tag.contains("breeze_size", 3)) breeze.hallelugw$setSize(tag.getInt("breeze_size"));
    }
}
