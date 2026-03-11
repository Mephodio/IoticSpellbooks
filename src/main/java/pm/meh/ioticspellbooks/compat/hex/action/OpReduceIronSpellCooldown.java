package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.capabilities.magic.CooldownInstance;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.RenderedSpellJava;
import pm.meh.ioticspellbooks.compat.hex.iface.SpellActionJava;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

import java.util.List;

public class OpReduceIronSpellCooldown implements SpellActionJava {
    @Override
    public int getArgc() {
        return 2;
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
        var spellData = IronSpellIota.getFromStack(list, 0, getArgc());
        var target = OperatorUtils.getPlayer(list, 1, getArgc());
        castingEnvironment.assertEntityInRange(target);

        var spell = spellData.getSpell();
        var cooldowns = MagicData.getPlayerMagicData(target).getPlayerCooldowns();
        var currentCooldown = cooldowns.getSpellCooldowns().getOrDefault(
                spell.getSpellId(), new CooldownInstance(0)).getCooldownRemaining();

        if (currentCooldown <= 0) {
            return new Result(
                    (RenderedSpellJava) castingEnvironment1 -> {},
                    0,
                    List.of(),
                    2);
        }

        var fullCooldown = MagicManager.getEffectiveSpellCooldown(spell, target, CastSource.MOB);
        var cost = MediaConstants.CRYSTAL_UNIT * fullCooldown / currentCooldown;

        return new Result(
                new Spell(spell.getSpellId(), target, cooldowns, currentCooldown),
                cost,
                List.of(ParticleSpray.burst(target.position(), 2, 50)),
                2);
    }

    private record Spell(String spellId, ServerPlayer serverPlayer, PlayerCooldowns cooldowns,
                         int currentCooldown) implements RenderedSpellJava {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            var reduction = (currentCooldown + 1) / 2;

            if (reduction >= currentCooldown) {
                cooldowns.removeCooldown(spellId);
            } else {
                cooldowns.getSpellCooldowns().getOrDefault(spellId,
                        new CooldownInstance(0)).decrementBy(reduction);
            }
            cooldowns.syncToPlayer(serverPlayer);
        }
    }
}
