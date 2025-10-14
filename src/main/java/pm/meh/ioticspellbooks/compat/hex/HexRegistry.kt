package pm.meh.ioticspellbooks.compat.hex

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.registries.RegisterEvent
import pm.meh.ioticspellbooks.IoticSpellbooks
import pm.meh.ioticspellbooks.compat.hex.action.OpCastIronSpell

class HexRegistry {
    fun register(event: RegisterEvent) {
        val key = event.registryKey
        if (key == HexRegistries.ACTION) {
            registerActions()
        }
    }

    private fun registerActions() {
        val registry = HexActions.REGISTRY
        val pattern = HexPattern.fromAngles("aaeedqeqaqdeewwdwwwwewwdwww", HexDir.WEST)

        Registry.register(registry,
            ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "cast_iron_spell"),
            ActionRegistryEntry(pattern, OpCastIronSpell))
    }
}