package pm.meh.ioticspellbooks;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import pm.meh.ioticspellbooks.compat.hex.HexRegistry;
import pm.meh.ioticspellbooks.compat.iron.IoticSpellRegistry;
import pm.meh.ioticspellbooks.entity.ConjuredSpellbookEntity;
import pm.meh.ioticspellbooks.entity.ConjuredSpellbookEntityRenderer;
import pm.meh.ioticspellbooks.entity.ConjuredSpellbookModel;
import pm.meh.ioticspellbooks.network.PacketHandler;

@Mod(IoticSpellbooks.MODID)
public class IoticSpellbooks {
    public static final String MODID = "ioticspellbooks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<ConjuredSpellbookEntity>> CONJURED_SPELLBOOK =
            ENTITY_TYPES.register("conjured_spellbook", () -> EntityType.Builder.of(ConjuredSpellbookEntity::new, MobCategory.MISC)
                    .sized(1, 1).build("conjured_spellbook"));

    public IoticSpellbooks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        IoticSpellRegistry.register(modEventBus);

        var hexRegistry = new HexRegistry();
        modEventBus.addListener(hexRegistry::register);

        ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(IoticSpellbooks::registerEntityAttributes);

        modEventBus.addListener(PacketHandler::init);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(CONJURED_SPELLBOOK.get(), ConjuredSpellbookEntity.prepareAttributes().build());
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(CONJURED_SPELLBOOK.get(), ConjuredSpellbookEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ConjuredSpellbookModel.LAYER_LOCATION,
                    ConjuredSpellbookModel::createBodyLayer);
        }
    }
}
