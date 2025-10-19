package pm.meh.ioticspellbooks.entity;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.Collections;

public class ConjuredSpellbookEntity extends AbstractSpellCastingMob {

    private int OPEN_ANIMATION_DURATION = 20;

    private final MagicData playerMagicData = new MagicData(true);

    public final AnimationState openAnimationState = new AnimationState();
    public final AnimationState closeAnimationState = new AnimationState();
    public final AnimationState castAnimationState = new AnimationState();
    private int openAnimationTimeout = 0;
    private boolean isCasting = false;
    private boolean wasCasting = false;
    private boolean queryStopCasting = false;

    public ConjuredSpellbookEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        super.initiateCastSpell(spell, spellLevel);

        isCasting = true;
    }

    @Override
    public void castComplete() {
        super.castComplete();

        queryStopCasting = true;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {}

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.AMETHYST_CLUSTER_HIT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.AMETHYST_CLUSTER_BREAK;
    }

    public void forceLookAtTarget(Vec3 target) {
        if (target != null) {
            double d0 = target.x;
            double d1 = target.y;
            double d2 = target.z;

            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
            float f1 = (float) (-(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI)));
            this.setXRot(f1 % 360);
            this.setYRot(f % 360);
        }
    }

    private void setupAnimationStates() {
        if (isCasting) {
            if (!wasCasting) {
                openAnimationTimeout = OPEN_ANIMATION_DURATION;
                wasCasting = true;
                closeAnimationState.stop();
                openAnimationState.start(tickCount);
            } else if (openAnimationTimeout > 0) {
                openAnimationTimeout--;
                if (openAnimationTimeout <= 0) {
                    openAnimationState.stop();
                    if (queryStopCasting) {
                        queryStopCasting = false;
                        isCasting = false;
                    } else {
                        castAnimationState.start(tickCount);
                    }
                }
            } else if (queryStopCasting) {
                queryStopCasting = false;
                isCasting = false;
            }
        }
        if (!isCasting) {
            if (wasCasting) {
                openAnimationTimeout = OPEN_ANIMATION_DURATION;
                wasCasting = false;
                castAnimationState.stop();
                closeAnimationState.start(tickCount);
            } else if (openAnimationTimeout > 0) {
                openAnimationTimeout--;
                if (openAnimationTimeout <= 0) {
                    closeAnimationState.stop();
                }
            }
        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FOLLOW_RANGE, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0);
    }
}
