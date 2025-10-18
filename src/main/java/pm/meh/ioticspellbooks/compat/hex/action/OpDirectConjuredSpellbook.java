package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.RenderedSpellJava;
import pm.meh.ioticspellbooks.compat.hex.iface.SpellActionJava;
import pm.meh.ioticspellbooks.entity.ConjuredSpellbookEntity;

import java.util.List;

public class OpDirectConjuredSpellbook implements SpellActionJava {
    @Override
    public int getArgc() {
        return 2;
    }

    @Override
    public boolean hasCastingSound(@NotNull CastingEnvironment castingEnvironment) {
        return true;
    }

    @Override
    public boolean awardsCastingStat(@NotNull CastingEnvironment castingEnvironment) {
        return true;
    }

    @Override
    public @NotNull Result execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (list.size() < getArgc()) {
            throw new MishapNotEnoughArgs(getArgc(), list.size());
        }
        var spellbookIota = list.get(0);
        if (spellbookIota instanceof EntityIota entityIota
                && entityIota.getEntity() instanceof ConjuredSpellbookEntity spellbookEntity) {
            castingEnvironment.assertEntityInRange(spellbookEntity);

            var targetIota = list.get(1);
            LivingEntity targetEntity = null;
            Vec3 targetVec3 = null;
            boolean clearTarget = false;

            if (targetIota instanceof EntityIota
                    && ((EntityIota) targetIota).getEntity() instanceof LivingEntity targetEntity2) {
                targetEntity = targetEntity2;
            } else if (targetIota instanceof Vec3Iota vec3Iota) {
                targetVec3 = vec3Iota.getVec3();
            } else if (targetIota instanceof NullIota) {
                clearTarget = true;
            }

            if (targetEntity == null && targetVec3 == null && !clearTarget) {
                throw MishapInvalidIota.ofType(targetIota, 0, "entity.living.or.vec");
            }

            return new Result(
                    new Spell(spellbookEntity, targetEntity, targetVec3),
                    MediaConstants.DUST_UNIT,
                    List.of(ParticleSpray.burst(spellbookEntity.position(), 2, 50)),
                    2);
        }
        throw MishapInvalidIota.ofType(spellbookIota, 1, "entity.conjured_spellbook");
    }

    private record Spell(ConjuredSpellbookEntity spellbookEntity, LivingEntity targetEntity, Vec3 targetVec3) implements RenderedSpellJava {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            if (targetEntity != null) {
                spellbookEntity.setTarget(targetEntity);
            } else if (targetVec3 != null) {
                spellbookEntity.setTarget(null);
                spellbookEntity.forceLookAtTarget(targetVec3);
            } else {
                spellbookEntity.setTarget(null);
            }
        }
    }
}
