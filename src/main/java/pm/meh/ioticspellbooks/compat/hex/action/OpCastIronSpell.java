package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.RenderedSpellJava;
import pm.meh.ioticspellbooks.compat.hex.iface.SpellActionJava;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;
import pm.meh.ioticspellbooks.compat.hex.mishap.MishapIronSpellCast;

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
        var spellData = IronSpellIota.getFromStack(list, 0, getArgc());
        var target = OperatorUtils.getLivingEntityButNotArmorStand(list, 1, getArgc());

        var spell = spellData.getSpell();

        LivingEntity castingEntity = castingEnvironment.getCastingEntity();
        ServerPlayer castingPlayer = null;
        if (castingEntity instanceof ServerPlayer serverPlayer) {
            castingPlayer = serverPlayer;
            var magicData = MagicData.getPlayerMagicData(serverPlayer);
            CastResult castResult = spell.canBeCastedBy(spellData.getLevel(),
                    CastSource.SPELLBOOK,
                    magicData,
                    serverPlayer);
            if (!castResult.isSuccess()) {
                throw new MishapIronSpellCast(spell, castResult.message);
            }
        } else {
            throw new MishapIronSpellCast(spell, Component.translatable("ioticspellbooks.mishap.iron_spell_cast.unattended"));
        }

        var spellCost = Math.min(MediaConstants.CRYSTAL_UNIT,
                MediaConstants.DUST_UNIT * Math.max(1, spell.getManaCost(spellData.getLevel()) / 50));

        return new SpellAction.Result(
                new Spell(spellData, target, castingEnvironment.getWorld(), castingPlayer),
                spellCost,
                Collections.emptyList(),
                2);
    }

    private record Spell(SpellData spellData, LivingEntity target, Level level, ServerPlayer castingPlayer) implements RenderedSpellJava {
        @Override
        public void cast(@NotNull CastingEnvironment castingEnvironment) {
            int spellLevel = spellData.getLevel();
            AbstractSpell spell = spellData.getSpell();
            boolean success = false;

            if (target instanceof Player player) {
                success = spell.attemptInitiateCast(
                        ItemStack.EMPTY,
                        spell.getLevelFor(spellData.getLevel(), target),
                        level,
                        player,
                        CastSource.MOB,
                        false,
                        "hex");
            } else if (target instanceof IMagicEntity castingMob) {
                castingMob.initiateCastSpell(spell, spellLevel);
                success = castingMob.isCasting();
            } else {
                var magicData = MagicData.getPlayerMagicData(target);

                if (spell.checkPreCastConditions(level, spellLevel, target, magicData)) {
                    spell.onCast(level, spellLevel, target, CastSource.MOB, magicData);
                    spell.onServerCastComplete(level, spellLevel, target, magicData, false);
                    success = true;
                }
            }

            if (success) {
                var magicData = MagicData.getPlayerMagicData(castingPlayer);
                MagicHelper.MAGIC_MANAGER.addCooldown(castingPlayer, spell, CastSource.SPELLBOOK);

                var newMana = Math.max(magicData.getMana() - spell.getManaCost(spellData.getLevel()), 0);
                magicData.setMana(newMana);
                Messages.sendToPlayer(new ClientboundSyncMana(magicData), castingPlayer);
            }
        }
    }
}
