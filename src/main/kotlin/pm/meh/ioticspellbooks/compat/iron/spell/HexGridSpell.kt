package pm.meh.ioticspellbooks.compat.iron.spell

import at.petrak.hexcasting.common.lib.HexItems
import io.redspace.ironsspellbooks.api.config.DefaultConfig
import io.redspace.ironsspellbooks.api.magic.MagicData
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry
import io.redspace.ironsspellbooks.api.spells.*
import io.redspace.ironsspellbooks.api.util.AnimationHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import pm.meh.ioticspellbooks.IoticSpellbooks

@AutoSpellConfig
class HexGridSpell : AbstractSpell() {
    private val spellId: ResourceLocation = ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "hex_grid")

    private val defaultConfig: DefaultConfig? = DefaultConfig()
        .setMinRarity(SpellRarity.RARE)
        .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
        .setMaxLevel(5)
        .setCooldownSeconds(5.0)
        .build()

    init {
        this.manaCostPerLevel = 5
        this.baseSpellPower = 1
        this.spellPowerPerLevel = 1
        this.castTime = 0
        this.baseManaCost = 25
    }

    override fun getSpellResource(): ResourceLocation {
        return spellId
    }

    override fun getDefaultConfig(): DefaultConfig? {
        return defaultConfig
    }

    override fun getCastType(): CastType {
        return CastType.INSTANT
    }

    override fun getCastStartAnimation(): AnimationHolder {
        return SpellAnimations.SELF_CAST_ANIMATION
    }

    override fun onCast(
        level: Level,
        spellLevel: Int,
        entity: LivingEntity?,
        castSource: CastSource?,
        playerMagicData: MagicData?
    ) {
        if (entity is Player) {
            // TODO: grid closes instantly if not holding hex staff in offhand
            HexItems.STAFF_OAK.use(level, entity, InteractionHand.OFF_HAND)
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData)
    }
}