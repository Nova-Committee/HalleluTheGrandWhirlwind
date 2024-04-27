package committee.nova.hallelugw.mixin;

import committee.nova.hallelugw.api.ExtendedBreeze;
import committee.nova.hallelugw.api.ExtendedBreezeWindCharge;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.breeze.Shoot;
import net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Shoot.class)
public abstract class MixinShoot {
    @Redirect(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/breeze/Breeze;J)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/entity/monster/breeze/Breeze;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/projectile/windcharge/BreezeWindCharge;"
            )
    )
    private BreezeWindCharge redirect$tick(Breeze breeze, Level level) {
        final BreezeWindCharge charge = new BreezeWindCharge(breeze, level);
        ((ExtendedBreezeWindCharge) charge).hallelugw$setDamage(((ExtendedBreeze) breeze).hallelugw$getScaleF());
        return charge;
    }
}
