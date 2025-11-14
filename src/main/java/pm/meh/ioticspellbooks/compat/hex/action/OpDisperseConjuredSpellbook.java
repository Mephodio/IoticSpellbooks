package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import at.petrak.hexcasting.api.misc.MediaConstants;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.RenderedSpellJava;
import pm.meh.ioticspellbooks.compat.hex.iface.SpellActionJava;
import pm.meh.ioticspellbooks.entity.ConjuredSpellbookEntity;

import java.util.List;

public class OpDisperseConjuredSpellbook implements SpellActionJava {
    @Override
    public int getArgc() {
        return 1;
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

            return new SpellAction.Result(
                    new Spell(spellbookEntity),
                    spellbookEntity.isOwner(castingEnvironment.getCastingEntity()) ? MediaConstants.DUST_UNIT / 1000 : MediaConstants.CRYSTAL_UNIT,
                    List.of(ParticleSpray.burst(spellbookEntity.position(), 2, 50)),
                    1);
        }
        throw MishapInvalidIota.ofType(spellbookIota, 0, "entity.conjured_spellbook");
    }

    private record Spell(ConjuredSpellbookEntity spellbookEntity) implements RenderedSpellJava {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            spellbookEntity.discard();
        }
    }
}
