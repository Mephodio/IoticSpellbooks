package pm.meh.ioticspellbooks.compat.iron;

import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;

public class IronUtil {
    public static double getManaCost(SpellData spellData) {
        var spell = spellData.getSpell();
        var rawManaCost = spell.getManaCost(spellData.getLevel());

        return spell.getCastType() == CastType.CONTINUOUS ?
                (double) rawManaCost / MagicManager.CONTINUOUS_CAST_TICK_INTERVAL
                : rawManaCost;
    }
}
