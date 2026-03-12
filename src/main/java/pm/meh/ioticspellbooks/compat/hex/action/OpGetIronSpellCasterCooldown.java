package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionJava;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

import java.util.List;

public class OpGetIronSpellCasterCooldown implements ConstMediaActionJava {
    @Override
    public int getArgc() {
        return 2;
    }

    @Override
    public long getMediaCost() {
        return MediaConstants.DUST_UNIT;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        var spellData = IronSpellIota.getFromStack(list, 0, getArgc());
        var target = OperatorUtils.getLivingEntityButNotArmorStand(list, 1, getArgc());
        castingEnvironment.assertEntityInRange(target);

        var spell = spellData.getSpell();
        var playerMagicData = MagicData.getPlayerMagicData(target);
        var remainingCooldown = playerMagicData.getPlayerCooldowns().getSpellCooldowns().getOrDefault(
                spell.getSpellId(), new CooldownInstance(0)).getCooldownRemaining();

        return List.of(new DoubleIota(remainingCooldown));
    }
}
