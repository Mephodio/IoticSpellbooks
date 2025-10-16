package pm.meh.ioticspellbooks;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import pm.meh.ioticspellbooks.compat.hex.HexRegistry;
import pm.meh.ioticspellbooks.compat.iron.IoticSpellRegistry;

@Mod(IoticSpellbooks.MODID)
public class IoticSpellbooks {
    public static final String MODID = "ioticspellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IoticSpellbooks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        IoticSpellRegistry.register(modEventBus);

        var hexRegistry = new HexRegistry();
        modEventBus.addListener(hexRegistry::register);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
