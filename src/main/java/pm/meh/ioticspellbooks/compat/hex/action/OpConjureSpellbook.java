package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.compat.hex.iface.RenderedSpellJava;
import pm.meh.ioticspellbooks.compat.hex.iface.SpellActionJava;
import pm.meh.ioticspellbooks.entity.ConjuredSpellbookEntity;

import java.util.List;

public class OpConjureSpellbook implements SpellActionJava {
    @Override
    public int getArgc() {
        return 1;
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
        var pos = OperatorUtils.getBlockPos(list, 0, getArgc());
        castingEnvironment.assertVecInRange(pos.getCenter());

        return new SpellAction.Result(
                new Spell(pos, castingEnvironment.getWorld(), castingEnvironment.getCastingEntity()),
                MediaConstants.SHARD_UNIT,
                List.of(ParticleSpray.burst(pos.getCenter(), 2, 50)),
                1);
    }

    private record Spell(BlockPos pos, Level level, LivingEntity castingEntity) implements RenderedSpellJava {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            if (level instanceof ServerLevel serverLevel) {
                ConjuredSpellbookEntity entity = IoticSpellbooks.CONJURED_SPELLBOOK.get().spawn(serverLevel, pos, MobSpawnType.MOB_SUMMONED);
                if (entity != null) {
                    entity.setOwner(castingEntity);
                }
            }
        }
    }
}
