package pm.meh.ioticspellbooks

import com.mojang.logging.LogUtils
import net.minecraftforge.fml.common.Mod
import org.slf4j.Logger
import pm.meh.ioticspellbooks.compat.hex.HexRegistry
import pm.meh.ioticspellbooks.compat.iron.IronSpellRegistry
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(IoticSpellbooks.MODID)
object IoticSpellbooks {
    const val MODID = "ioticspellbooks"
    val LOGGER: Logger = LogUtils.getLogger()

    init {
        IronSpellRegistry.register(MOD_BUS)
//        MOD_BUS.addListener(HexRegistry::register)
    }
}