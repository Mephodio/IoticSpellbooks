package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.compat.hex.iface.RenderedSpellJava;
import pm.meh.ioticspellbooks.compat.hex.iface.SpellActionJava;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

import java.util.Collections;
import java.util.List;

public class OpCastIronSpell implements SpellActionJava {
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

        var spell = IronSpellIota.getFromStack(list, 0, getArgc());
        var target = OperatorUtils.getLivingEntityButNotArmorStand(list, 1, getArgc());

        return new SpellAction.Result(
                new Spell(spell, target, castingEnvironment.getWorld()),
                MediaConstants.SHARD_UNIT,
                Collections.emptyList(),
                2);
    }

    private record Spell(SpellData spellData, LivingEntity target, Level level) implements RenderedSpellJava {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            int spellLevel = spellData.getLevel();
            AbstractSpell spell = spellData.getSpell();

            if (target instanceof Player player) {
                spell.attemptInitiateCast(
                        ItemStack.EMPTY,
                        spell.getLevelFor(spellData.getLevel(), target),
                        level,
                        player,
                        CastSource.SPELLBOOK,
                        false,
                        "hex");
            } else if (target instanceof IMagicEntity castingMob) {
                castingMob.initiateCastSpell(spell, spellLevel);
            } else {
                var magicData = MagicData.getPlayerMagicData(target);

                if (!spell.checkPreCastConditions(level, spellLevel, target, magicData)) {
                    return;
                }

                spell.onCast(level, spellLevel, target, CastSource.MOB, magicData);
                spell.onServerCastComplete(level, spellLevel, target, magicData, false);
            }
        }
    }
}
