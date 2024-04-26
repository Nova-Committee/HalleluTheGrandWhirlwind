package committee.nova.hallelugw.mixin;

import committee.nova.hallelugw.api.ExtendedBreezeWindCharge;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractWindCharge.class)
public abstract class MixinAbstractWindCharge extends AbstractHurtingProjectile implements ExtendedBreezeWindCharge {
    @Unique
    private float hallelugw$damage = 1.0f;

    protected MixinAbstractWindCharge(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public float hallelugw$getDamage() {
        return this.hallelugw$damage;
    }

    @Override
    public void hallelugw$setDamage(float damage) {
        this.hallelugw$damage = damage;
    }

    @ModifyConstant(method = "onHitEntity", constant = @Constant(floatValue = 1.0f))
    private float modify$onHitEntity(float constant) {
        return this.hallelugw$damage;
    }
}
