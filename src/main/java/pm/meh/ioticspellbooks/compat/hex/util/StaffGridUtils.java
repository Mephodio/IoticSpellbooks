package pm.meh.ioticspellbooks.compat.hex.util;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.HexAttributes;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.msgs.MsgClearSpiralPatternsS2C;
import at.petrak.hexcasting.common.msgs.MsgOpenSpellGuiS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import pm.meh.ioticspellbooks.compat.hex.msg.MsgCloseSpellGuiS2C;
import pm.meh.ioticspellbooks.compat.hex.msg.MsgOpenWandlessSpellGuiS2C;
import pm.meh.ioticspellbooks.network.PacketHandler;

import java.util.List;

public class StaffGridUtils {
    public static void clearGrid(ServerPlayer serverPlayer) {
        var vm = IXplatAbstractions.INSTANCE.getStaffcastVM(serverPlayer, InteractionHand.MAIN_HAND);
        if (!vm.getImage().getStack().isEmpty()) {
            IXplatAbstractions.INSTANCE.clearCastingData(serverPlayer);
            var packet = new MsgClearSpiralPatternsS2C(serverPlayer.getUUID());
            IXplatAbstractions.INSTANCE.sendPacketToPlayer(serverPlayer, packet);
            IXplatAbstractions.INSTANCE.sendPacketTracking(serverPlayer, packet);
        }
    }

    public static void closeGrid(ServerPlayer serverPlayer) {
        PacketHandler.sendPacketToPlayer(serverPlayer, new MsgCloseSpellGuiS2C());
    }

    public static void openGridWandless(Level level, Player player, boolean clearOnShift) {
        if (player.getAttributeValue(HexAttributes.FEEBLE_MIND) > 0) {
            return;
        }
        if (clearOnShift && player.isShiftKeyDown()) {
            if (level.isClientSide()) {
                player.playSound(HexSounds.STAFF_RESET, 1f, 1f);
            } else if (player instanceof ServerPlayer serverPlayer) {
                clearGrid(serverPlayer);
            }
        }
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            var vm = IXplatAbstractions.INSTANCE.getStaffcastVM(serverPlayer, InteractionHand.OFF_HAND);
            var patterns = IXplatAbstractions.INSTANCE.getPatternsSavedInUi(serverPlayer);
            var stack = vmSerializeStack(vm);
            var ravenmind = vmSerializeRavenmind(vm);

            PacketHandler.sendPacketToPlayer(serverPlayer, new MsgOpenWandlessSpellGuiS2C(
                    new MsgOpenSpellGuiS2C(InteractionHand.OFF_HAND, patterns, stack, ravenmind, 0)
            ));
        }
    }

    public static List<CompoundTag> vmSerializeStack(CastingVM vm) {
        return vm.getImage().getStack().stream().map(IotaType::serialize).toList();
    }

    public static @Nullable CompoundTag vmSerializeRavenmind(CastingVM vm) {
        return vm.getImage().getUserData().contains(HexAPI.RAVENMIND_USERDATA) ?
                vm.getImage().getUserData().getCompound(HexAPI.RAVENMIND_USERDATA) : null;
    }
}
