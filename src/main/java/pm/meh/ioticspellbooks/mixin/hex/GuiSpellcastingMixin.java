package pm.meh.ioticspellbooks.mixin.hex;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.common.lib.HexAttributes;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pm.meh.ioticspellbooks.iface.IGuiSpellcastingMixin;

@Mixin(GuiSpellcasting.class)
public class GuiSpellcastingMixin implements IGuiSpellcastingMixin {
    @Unique
    private boolean ioticspellbooks$isWandless = false;

    @Unique @Override
    public void ioticspellbooks$setWandless(boolean value) {
        ioticspellbooks$isWandless = value;
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = false, cancellable = true)
    void onTick(CallbackInfo ci) {
        if (ioticspellbooks$isWandless) {
            var player = Minecraft.getInstance().player;
            if (player != null && player.getAttributeValue(HexAttributes.FEEBLE_MIND) <= 0) {
                ci.cancel();
            }
        }
    }
}
