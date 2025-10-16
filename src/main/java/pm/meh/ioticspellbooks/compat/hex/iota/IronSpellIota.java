package pm.meh.ioticspellbooks.compat.hex.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import at.petrak.hexcasting.api.utils.HexUtils;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IronSpellIota extends Iota {

    public static final String SPELL_ID = "id";
    public static final String SPELL_LEVEL = "level";
    public static final String SPELL_LOCKED = "locked";

    public IronSpellIota(@NotNull Object payload) {
        super(TYPE, payload);
    }

    public SpellData getSpellData() {
        return (SpellData) payload;
    }

    public static SpellData getFromStack(List<? extends Iota> stack, int idx, int argc) {
        if (idx >= stack.size()) {
            throw new MishapNotEnoughArgs(idx + 1, stack.size());
        }
        var x = stack.get(idx);
        if (x instanceof IronSpellIota ironSpellIota) {
            return ironSpellIota.getSpellData();
        }

        throw MishapInvalidIota.ofType(x, (argc == 0) ? idx : (argc - idx - 1), "iron_spell");
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof IronSpellIota thatSpell
                && this.getSpellData() == thatSpell.getSpellData();
    }

    @Override
    public @NotNull Tag serialize() {
        SpellData spellData = getSpellData();
        CompoundTag out = new CompoundTag();
        out.putString(SPELL_ID, spellData.getSpell().getSpellId());
        out.putInt(SPELL_LEVEL, spellData.getLevel());
        out.putBoolean(SPELL_LOCKED, spellData.isLocked());
        return out;
    }

    public static final IotaType<IronSpellIota> TYPE = new IotaType<>() {

        @Override
        public @Nullable IronSpellIota deserialize(Tag tag, ServerLevel serverLevel) throws IllegalArgumentException {
            SpellData spellData = deserializeSpellData(tag);
            if (spellData == null) {
                return null;
            }
            return new IronSpellIota(spellData);
        }

        @Override
        public Component display(Tag tag) {
            SpellData spellData = deserializeSpellData(tag);
            if (spellData == null) {
                return Component.translatable("ioticspellbooks.iota.iron_spell.invalid");
            }
            return Component.translatable("ioticspellbooks.iota.iron_spell.display",
                    spellData.getSpell().getDisplayName(null), spellData.getLevel())
                    .withStyle(spellData.getSpell().getSchoolType().getDisplayName().getStyle());
        }

        @Override
        public int color() {
            return 0x4B98C1;
        }

        private static @Nullable SpellData deserializeSpellData(Tag tag) {
            if (tag instanceof CompoundTag ctag) {
                String id = ctag.getString(SPELL_ID);
                int level = ctag.getInt(SPELL_LEVEL);
                boolean locked = ctag.getBoolean(SPELL_LOCKED);

                if (id.isEmpty() || level == 0) {
                    return null;
                }

                return new SpellData(SpellRegistry.getSpell(id), level, locked);
            }
            return null;
        }
    };
}
