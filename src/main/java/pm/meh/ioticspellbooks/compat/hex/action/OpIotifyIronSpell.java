package pm.meh.ioticspellbooks.compat.hex.action;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.compat.hex.iface.ConstMediaActionJava;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

import java.util.List;

public class OpIotifyIronSpell implements ConstMediaActionJava {
    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public long getMediaCost() {
        return MediaConstants.SHARD_UNIT;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        var handStack = castingEnvironment.getHeldItemToOperateOn(stack -> stack.is(ItemRegistry.SCROLL.get()));

        if (handStack == null) {
            throw MishapBadOffhandItem.of(null, "iron_scroll");
        }

        SpellData spellData = ISpellContainer.get(handStack.stack()).getSpellAtIndex(0);
        return List.of(new IronSpellIota(spellData));
    }
}
