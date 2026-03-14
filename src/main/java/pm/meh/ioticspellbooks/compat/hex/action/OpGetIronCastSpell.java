package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionJava;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;
import pm.meh.ioticspellbooks.compat.iron.IronUtil;

import java.util.List;

public class OpGetIronCastSpell implements ConstMediaActionJava {
    @Override
    public int getArgc() {
        return 1;
    }

    @Override
    public long getMediaCost() {
        return MediaConstants.SHARD_UNIT;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        var target = OperatorUtils.getLivingEntityButNotArmorStand(list, 0, getArgc());
        castingEnvironment.assertEntityInRange(target);

        var magicData = IronUtil.getMagicData(target);

        var result = magicData.isCasting() ? new IronSpellIota(magicData.getCastingSpell())
                : new NullIota();

        return List.of(result);
    }
}
