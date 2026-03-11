package pm.meh.ioticspellbooks.entity;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.Collections;
import java.util.UUID;

public class ConjuredSpellbookEntity extends AbstractSpellCastingMob implements AntiMagicSusceptible {
    private final int OPEN_ANIMATION_DURATION = 20;
    private final int CLOSE_ANIMATION_DURATION = 4;
    private final String NBT_KEY_OWNER = "Owner";

    public final AnimationState openAnimationState = new AnimationState();
    public final AnimationState closeAnimationState = new AnimationState();
    public final AnimationState castAnimationState = new AnimationState();
    private int openAnimationTimeout = 0;
    private boolean isCasting = false;
    private boolean wasCasting = false;
    private boolean queryStopCasting = false;
    private boolean isTemp = false;
    private UUID ownerUuid;

    public ConjuredSpellbookEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();

        lookAtTarget(getTarget());

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

        if (isTemp) {
            this.discard();
            if (level() instanceof ServerLevel serverLevel) {
                ParticleSpray.burst(position(), 1, 50).sprayParticles(serverLevel, FrozenPigment.DEFAULT.get());
            }
        }
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl(this) {
            @Override
            protected boolean resetXRotOnTick() {
                return false;
            }
        };
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

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        if (ownerUuid != null) {
            pCompound.putUUID(NBT_KEY_OWNER, ownerUuid);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.hasUUID(NBT_KEY_OWNER)) {
            ownerUuid = pCompound.getUUID(NBT_KEY_OWNER);
        }
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        discard();
    }

    public boolean isOwner(@Nullable Entity entity) {
        return entity != null && entity.getUUID().equals(ownerUuid);
    }

    public void setOwner(@Nullable Entity entity) {
        ownerUuid = entity == null ? null : entity.getUUID();
    }

    public void makeTemp() {
        isTemp = true;
    }

    public void lookAtTarget(Vec3 target) {
        if (target != null) {
            lookAt(EntityAnchorArgument.Anchor.EYES, target);
        }
    }

    private void lookAtTarget(LivingEntity target) {
        if (target != null) {
            lookAtTarget(target.getEyePosition());
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
                openAnimationTimeout = CLOSE_ANIMATION_DURATION;
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
