package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionJava;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;
import pm.meh.ioticspellbooks.compat.hex.mishap.MishapIronSpellCast;

import java.util.List;

public class OpStopIronCasting implements ConstMediaActionJava {
    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public long getMediaCost() {
        return MediaConstants.DUST_UNIT;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (castingEnvironment.getCastingEntity() instanceof ServerPlayer serverPlayer) {
            var magicData = MagicData.getPlayerMagicData(serverPlayer);
            if (magicData.isCasting()) {
                Utils.serverSideCancelCast(serverPlayer);
            }
        }

        return List.of();
    }
}
