package pm.meh.ioticspellbooks.compat.hex;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.HexRegistries;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.compat.hex.action.OpCastIronSpell;
import pm.meh.ioticspellbooks.compat.hex.action.OpConjureSpellbook;
import pm.meh.ioticspellbooks.compat.hex.action.OpDisperseConjuredSpellbook;
import pm.meh.ioticspellbooks.compat.hex.action.OpIotifyIronSpell;
import pm.meh.ioticspellbooks.compat.hex.iota.IronSpellIota;

public class HexRegistry {
    public void register(RegisterEvent event) {
        if (event.getRegistryKey() == HexRegistries.ACTION) {
            registerActions();
        } else if (event.getRegistryKey() == HexRegistries.IOTA_TYPE) {
            registerIotaTypes();
        }
    }

    private void registerActions() {
        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "cast_iron_spell"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwaewdeaqq", HexDir.SOUTH_WEST),
                        new OpCastIronSpell()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "iotify_iron_spell"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwawwaqqqqq", HexDir.SOUTH_WEST),
                        new OpIotifyIronSpell()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "conjure_spellbook"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("qaqwawqwwawq", HexDir.EAST),
                        new OpConjureSpellbook()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "disperse_spellbook"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("edeqawqwwawq", HexDir.NORTH_EAST),
                        new OpDisperseConjuredSpellbook()));
    }

    private void registerIotaTypes() {
        Registry.register(HexIotaTypes.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "iron_spell"),
                IronSpellIota.TYPE);
    }
}
