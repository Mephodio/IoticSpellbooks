package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionWithOptionalArgs;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

import java.util.List;

public class OpGetIronSpellCooldown implements ConstMediaActionWithOptionalArgs {

    ServerPlayer targetPlayer = null;
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
                    && entityIota.getEntity() instanceof ServerPlayer serverPlayer) {
                targetPlayer = serverPlayer;
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

        var cooldown = (targetPlayer == null) ? spell.getSpellCooldown()
                : MagicManager.getEffectiveSpellCooldown(spell, targetPlayer, CastSource.MOB);

        return List.of(new DoubleIota(cooldown));
    }
}
