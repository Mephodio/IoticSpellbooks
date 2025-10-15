package pm.meh.ioticspellbooks.compat.iron

import io.redspace.ironsspellbooks.api.registry.SpellRegistry
import io.redspace.ironsspellbooks.api.spells.AbstractSpell
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import pm.meh.ioticspellbooks.IoticSpellbooks
import pm.meh.ioticspellbooks.compat.iron.spell.HexGridSpell
import java.util.function.Supplier

object IronSpellRegistry {
    val SPELLS: DeferredRegister<AbstractSpell> =
        DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, IoticSpellbooks.MODID)

    fun register(eventBus: IEventBus?) {
        SPELLS.register(eventBus)
    }

    fun registerSpell(spell: AbstractSpell): RegistryObject<AbstractSpell?>? {
        return SPELLS.register(spell.getSpellName(), Supplier { spell })
    }

    val HEX_GRID_SPELL: RegistryObject<AbstractSpell?>? = registerSpell(HexGridSpell())
}