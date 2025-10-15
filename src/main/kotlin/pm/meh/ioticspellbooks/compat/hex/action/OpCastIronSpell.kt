package pm.meh.ioticspellbooks.compat.hex.action

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.world.entity.Entity

object OpCastIronSpell : SpellAction {
    override val argc = 0
    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        return SpellAction.Result(
            Spell(null),
            MediaConstants.SHARD_UNIT,
            emptyList()
        )
    }

    private data class Spell(val caster: Entity?) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            // TODO
        }
    }
}