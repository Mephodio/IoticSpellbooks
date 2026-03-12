package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionJava;

import java.util.List;

public class OpGetIronCastTimeRemaining implements ConstMediaActionJava {
    @Override
    public int getArgc() {
        return 1;
    }

    @Override
    public long getMediaCost() {
        return MediaConstants.DUST_UNIT;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        var target = OperatorUtils.getLivingEntityButNotArmorStand(list, 0, getArgc());
        castingEnvironment.assertEntityInRange(target);

        var magicData = MagicData.getPlayerMagicData(target);

        return List.of(new DoubleIota(magicData.getCastDurationRemaining()));
    }
}
