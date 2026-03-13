package pm.meh.ioticspellbooks.compat.iron;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.world.entity.LivingEntity;

public class IronUtil {
    public static double getManaCost(SpellData spellData) {
        var spell = spellData.getSpell();
        var rawManaCost = spell.getManaCost(spellData.getLevel());

        return spell.getCastType() == CastType.CONTINUOUS ?
                (double) rawManaCost / MagicManager.CONTINUOUS_CAST_TICK_INTERVAL
                : rawManaCost;
    }

    public static MagicData getMagicData(LivingEntity entity) {
        MagicData magicData = null;

        if (entity instanceof IMagicEntity magicEntity) {
            magicData = magicEntity.getMagicData();
        }

        if (magicData == null) {
            magicData = MagicData.getPlayerMagicData(entity);
        }

        return magicData;
    }
}
