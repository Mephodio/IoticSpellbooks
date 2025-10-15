package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.compat.hex.iface.RenderedSpellJava;
import pm.meh.ioticspellbooks.compat.hex.iface.SpellActionJava;

import java.util.Collections;
import java.util.List;

public class OpCastIronSpell implements SpellActionJava {
    @Override
    public int getArgc() {
        return 0;
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
        return new SpellAction.Result(
                new Spell(),
                MediaConstants.SHARD_UNIT,
                Collections.emptyList(),
                1);
    }

    private static class Spell implements RenderedSpellJava {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            IoticSpellbooks.LOGGER.info("hex casted");
        }
    }
}
