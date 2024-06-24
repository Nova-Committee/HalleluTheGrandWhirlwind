package committee.nova.hallelugw.mixin;

import committee.nova.hallelugw.api.ExtendedBreeze;
import committee.nova.hallelugw.util.Utilities;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Breeze.class)
public abstract class MixinBreeze extends Monster implements ExtendedBreeze {
    @Unique
    private int hallelugw$mergeTarget = -1;

    @Unique
    private int hallelugw$size0 = 10;

    @Unique
    private int hallelugw$size = 10;

    protected MixinBreeze(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int hallelugw$getSize() {
        return this.hallelugw$size;
    }

    @Override
    public void hallelugw$setSize(int size) {
        size = Mth.clamp(size, 10, 100);
        this.hallelugw$size = size;
        if (this.hallelugw$size0 != this.hallelugw$size) {
            final float healthRate = this.getHealth() / this.getMaxHealth();
            this.hallelugw$refreshAttributes();
            this.setHealth(this.getMaxHealth() * healthRate);
            this.hallelugw$size0 = this.hallelugw$size;
        }
    }

    @Unique
    private void hallelugw$refreshAttributes() {
        Utilities.modifyIfPresent(getAttribute(Attributes.MOVEMENT_SPEED), i -> i.setBaseValue(0.63f * hallelugw$getScaleF()));
        Utilities.modifyIfPresent(getAttribute(Attributes.MAX_HEALTH), i -> i.setBaseValue(30.0 * hallelugw$getScaleD()));
        Utilities.modifyIfPresent(getAttribute(Attributes.FOLLOW_RANGE), i -> i.setBaseValue(24.0 * hallelugw$getScaleD()));
        Utilities.modifyIfPresent(getAttribute(Attributes.ATTACK_DAMAGE), i -> i.setBaseValue(3.0 * hallelugw$getScaleD()));
        Utilities.modifyIfPresent(getAttribute(Attributes.SCALE), i -> i.setBaseValue(hallelugw$getScaleF()));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void inject$tick(CallbackInfo ci) {
        if (level().isClientSide) return;
        if ((tickCount + getId()) % 40 != 0) return;
        final int thisSize = hallelugw$getSize();
        if (hallelugw$mergeTarget != -1) {
            if (!(level().getEntity(hallelugw$mergeTarget) instanceof Breeze that)) {
                this.hallelugw$mergeTarget = -1;
                return;
            }
            final int thatSize = ((ExtendedBreeze) that).hallelugw$getSize();
            if (thatSize > thisSize) {
                this.hallelugw$mergeTarget = -1;
                return;
            }
            final int newSize = thatSize + thisSize;
            if (newSize > 100) {
                this.hallelugw$mergeTarget = -1;
                return;
            }
            that.discard();
            this.hallelugw$setSize(newSize);
        } else {
            level().getEntities(
                            EntityTypeTest.forExactClass(Breeze.class),
                            getBoundingBox().inflate(hallelugw$getScaleD() * 2),
                            b -> {
                                final int thatSize = ((ExtendedBreeze) b).hallelugw$getSize();
                                return thisSize + thatSize <= 100 && (thatSize < thisSize || (thatSize == thisSize && b.getId() < getId()));
                            }).stream()
                    .findFirst()
                    .ifPresent(b -> this.hallelugw$mergeTarget = b.getId());
        }
    }

    @Inject(method = "withinInnerCircleRange", at = @At("HEAD"), cancellable = true)
    private void inject$withinInnerCircleRange(Vec3 vec3, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(vec3.closerThan(this.blockPosition().getCenter(), 4.0 * Math.sqrt(hallelugw$getScaleD()), 10.0));
    }

    @ModifyVariable(method = "emitGroundParticles", at = @At("HEAD"), argsOnly = true)
    private int modify$emitGroundParticles(int value) {
        return value * hallelugw$getScaleI();
    }

    @ModifyConstant(method = "emitJumpTrailParticles", constant = @Constant(intValue = 3))
    private int modify$emitJumpTrailParticles(int constant) {
        return constant * hallelugw$getScaleI();
    }

}
