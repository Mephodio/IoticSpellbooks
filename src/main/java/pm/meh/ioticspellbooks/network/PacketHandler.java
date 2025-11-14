package pm.meh.ioticspellbooks.network;

import at.petrak.hexcasting.common.msgs.IMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.compat.hex.msgs.MsgCloseSpellGuiS2C;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void sendPacketToPlayer(ServerPlayer target, IMessage packet) {
        NETWORK.send(PacketDistributor.PLAYER.with(() -> target), packet);
    }

    public static SimpleChannel getNetwork() {
        return NETWORK;
    }

    public static void init(FMLCommonSetupEvent event) {
        int messageIdx = 0;

        NETWORK.registerMessage(messageIdx++, MsgCloseSpellGuiS2C.class, MsgCloseSpellGuiS2C::serialize,
                MsgCloseSpellGuiS2C::deserialize, makeClientBoundHandler(MsgCloseSpellGuiS2C::handle));
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> makeClientBoundHandler(Consumer<T> consumer) {
        return (m, ctx) -> {
            consumer.accept(m);
            ctx.get().setPacketHandled(true);
        };
    }
}
