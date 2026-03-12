package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionJava;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionWithOptionalArgs;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

import java.util.List;

public class OpGetIronSpellCastTime implements ConstMediaActionWithOptionalArgs {

    LivingEntity targetEntity = null;
    boolean drawOneMore = false;

    @Override
    public int getMinArgc() {
        return 1;
    }

    @Override
    public int getMaxArgc() {
        return 2;
    }

    @Override
    public boolean shouldUseArg(int index, Iota iota) {
        if (index == 0) {
            if (iota instanceof EntityIota entityIota
                    && entityIota.getEntity() instanceof LivingEntity livingEntity) {
                targetEntity = livingEntity;
                drawOneMore = true;
            }
            return true;
        }
        return index == 1 && drawOneMore;
    }

    @Override
    public long getMediaCost() {
        return 0;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        var spellData = IronSpellIota.getFromStack(list, 0, list.size());

        var spell = spellData.getSpell();

        var castTime = (targetEntity == null) ? spell.getCastTime(spellData.getLevel())
            : spell.getEffectiveCastTime(spellData.getLevel(), targetEntity);

        return List.of(new DoubleIota(castTime));
    }
}
