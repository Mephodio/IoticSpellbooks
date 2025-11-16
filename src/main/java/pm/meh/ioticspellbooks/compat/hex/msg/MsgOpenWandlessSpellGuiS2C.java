package pm.meh.ioticspellbooks.compat.hex.msg;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.common.msgs.IMessage;
import at.petrak.hexcasting.common.msgs.MsgOpenSpellGuiS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.iface.IGuiSpellcastingMixin;

public record MsgOpenWandlessSpellGuiS2C(MsgOpenSpellGuiS2C msgOpenSpellGuiS2C) implements IMessage {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "open_wandless_hexcasting_gui");

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        msgOpenSpellGuiS2C.serialize(buf);
    }

    public static MsgOpenWandlessSpellGuiS2C deserialize(FriendlyByteBuf buf) {
        return new MsgOpenWandlessSpellGuiS2C(MsgOpenSpellGuiS2C.deserialize(buf));
    }

    public static void handle(MsgOpenWandlessSpellGuiS2C msg) {
        Minecraft.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                var mc = Minecraft.getInstance();
                var gui = new GuiSpellcasting(msg.msgOpenSpellGuiS2C.hand(),
                                msg.msgOpenSpellGuiS2C.patterns(),
                                msg.msgOpenSpellGuiS2C.stack(),
                                msg.msgOpenSpellGuiS2C.ravenmind(),
                                msg.msgOpenSpellGuiS2C.parenCount()
                        );
                ((IGuiSpellcastingMixin) (Object) gui).ioticspellbooks$setWandless(true);
                mc.setScreen(gui);
            }
        });
    }
}
