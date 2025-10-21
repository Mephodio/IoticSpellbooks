package pm.meh.ioticspellbooks.compat.hex.mishap;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MishapIronSpellCast extends Mishap {
    private final AbstractSpell spell;
    private final Component message;

    public MishapIronSpellCast(AbstractSpell spell, Component message) {
        this.spell = spell;
        this.message = message;
    }

    @Override
    public @NotNull FrozenPigment accentColor(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        return dyeColor(DyeColor.MAGENTA);
    }

    @Override
    public void execute(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context, @NotNull List<Iota> list) {}

    @Override
    protected @Nullable Component errorMessage(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        return error("ioticspellbooks.iron_spell_cast", spell.getDisplayName(null), message);
    }
}
