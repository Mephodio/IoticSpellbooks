package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionWithOptionalArgs;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

import java.util.List;

public class OpGetIronSpellCastTime implements ConstMediaActionWithOptionalArgs {
    @Override
    public int getMinArgc() {
        return 1;
    }

    @Override
    public int getCurrentArgc(List<Iota> stack) {
        if (stack.get(stack.size() - 1) instanceof EntityIota entityIota
                && entityIota.getEntity() instanceof LivingEntity) {
            return 2;
        }
        return 1;
    }

    @Override
    public long getMediaCost() {
        return MediaConstants.DUST_UNIT;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        var spellData = IronSpellIota.getFromStack(list, 0, list.size());
        var spell = spellData.getSpell();
        int castTime;

        if (list.size() == 2) {
            var target = OperatorUtils.getLivingEntityButNotArmorStand(list, 1, list.size());
            castingEnvironment.assertEntityInRange(target);
            castTime = spell.getEffectiveCastTime(spellData.getLevel(), target);
        } else {
            castTime = spell.getCastTime(spellData.getLevel());
        }

        return List.of(new DoubleIota(castTime));
    }
}
