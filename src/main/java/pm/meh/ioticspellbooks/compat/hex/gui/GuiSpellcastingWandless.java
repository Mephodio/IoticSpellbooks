package pm.meh.ioticspellbooks.compat.hex.gui;

import at.petrak.hexcasting.api.casting.eval.ResolvedPattern;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.common.lib.HexSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuiSpellcastingWandless extends Screen {
    GuiSpellcasting guiSpellcasting;
    public GuiSpellcastingWandless(List<ResolvedPattern> patterns, List<CompoundTag> cachedStack,
                                   @Nullable CompoundTag cachedRavenmind, int parenCount) {
        super(Component.translatable("gui.hexcasting.spellcasting"));
        guiSpellcasting = new GuiSpellcasting(InteractionHand.OFF_HAND, patterns, cachedStack, cachedRavenmind, parenCount);
    }

    @Override
    protected void init() {
        var minecraft = Minecraft.getInstance();
        var soundManager = minecraft.getSoundManager();
        soundManager.stop(HexSounds.CASTING_AMBIANCE.getLocation(), null);
        var player = minecraft.player;
        if (player != null) {
//            this.ambianceSoundInstance = GridSoundInstance(player)
//            soundManager.play(this.ambianceSoundInstance!!)
        }
        guiSpellcasting.calculateIotaDisplays();
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        return guiSpellcasting.mouseClicked(p_94695_, p_94696_, p_94697_);
    }

    @Override
    public void mouseMoved(double p_94758_, double p_94759_) {
        guiSpellcasting.mouseMoved(p_94758_, p_94759_);
    }

    @Override
    public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
        return guiSpellcasting.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
    }

    @Override
    public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
        return guiSpellcasting.mouseReleased(p_94722_, p_94723_, p_94724_);
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        return guiSpellcasting.mouseScrolled(p_94686_, p_94687_, p_94688_);
    }

    @Override
    public void onClose() {
        guiSpellcasting.onClose();
    }

    @Override
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        guiSpellcasting.render(p_281549_, p_281550_, p_282878_, p_282465_);
    }

    @Override
    public boolean isPauseScreen() {
        return guiSpellcasting.isPauseScreen();
    }
}
