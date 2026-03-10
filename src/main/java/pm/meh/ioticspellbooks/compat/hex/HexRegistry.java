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
import pm.meh.ioticspellbooks.compat.hex.action.*;
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

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "direct_spellbook"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("qaqwawqwwawqewd", HexDir.EAST),
                        new OpDirectConjuredSpellbook()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_spell_cast_time"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwaadeeee", HexDir.SOUTH_WEST),
                        new OpGetIronSpellCastTime()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_spell_cooldown"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwaqwedwwedeada", HexDir.SOUTH_WEST),
                        new OpGetIronSpellCooldown()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_spell_cost"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwawwwdewwddadad", HexDir.SOUTH_WEST),
                        new OpGetIronSpellCost()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_spell_level"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwqwdwwwd", HexDir.SOUTH_WEST),
                        new OpGetIronSpellLevel()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_spell_max_level"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwqwwdwwwwdw", HexDir.SOUTH_WEST),
                        new OpGetIronSpellMaxLevel()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_spell_cast_type"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwaqwwaq", HexDir.SOUTH_WEST),
                        new OpGetIronSpellCastType()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "downgrade_iron_spell"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwadwewwwdwe", HexDir.SOUTH_WEST),
                        new OpDowngradeIronSpell()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_spell_caster_cooldown"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwewdwaqwedwwedea", HexDir.SOUTH_WEST),
                        new OpGetIronSpellCasterCooldown()));

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "get_iron_mana_max"),
                new ActionRegistryEntry(
                        HexPattern.fromAngles("adewdwwaddadadaddqw", HexDir.SOUTH_WEST),
                        new OpGetIronManaMax()));
    }

    private void registerIotaTypes() {
        Registry.register(HexIotaTypes.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "iron_spell"),
                IronSpellIota.TYPE);
    }
}
