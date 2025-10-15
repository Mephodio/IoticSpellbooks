package pm.meh.ioticspellbooks.compat.hex.iface;

import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RenderedSpellJava extends RenderedSpell {
    @Override
    @Nullable
    default CastingImage cast(@NotNull CastingEnvironment castingEnvironment, @NotNull CastingImage castingImage) {
        cast(castingEnvironment);
        return null;
    }
}
