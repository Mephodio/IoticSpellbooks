package pm.meh.ioticspellbooks.compat.hex.msg;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.common.msgs.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import pm.meh.ioticspellbooks.IoticSpellbooks;

public class MsgCloseSpellGuiS2C implements IMessage {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "close_hexcasting_gui");

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {}

    public static MsgCloseSpellGuiS2C deserialize(FriendlyByteBuf buf) {
        return new MsgCloseSpellGuiS2C();
    }

    public static void handle(MsgCloseSpellGuiS2C msg) {
        Minecraft.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                var mc = Minecraft.getInstance();
                if (mc.screen instanceof GuiSpellcasting) {
                    mc.setScreen(null);
                }
            }
        });
    }
}
