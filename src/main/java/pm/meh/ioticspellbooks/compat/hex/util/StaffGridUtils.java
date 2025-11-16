package pm.meh.ioticspellbooks.compat.hex.util;

import at.petrak.hexcasting.common.lib.HexAttributes;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.msgs.MsgClearSpiralPatternsS2C;
import at.petrak.hexcasting.common.msgs.MsgOpenSpellGuiS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import pm.meh.ioticspellbooks.compat.hex.msg.MsgCloseSpellGuiS2C;
import pm.meh.ioticspellbooks.network.PacketHandler;

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

    public static void openGridWithoutWand(Level world, Player player, boolean clearOnShift) {
        if (player.getAttributeValue(HexAttributes.FEEBLE_MIND) > 0) {
            return;
        }
        if (clearOnShift && player.isShiftKeyDown()) {
            if (world.isClientSide()) {
                player.playSound(HexSounds.STAFF_RESET, 1f, 1f);
            } else if (player instanceof ServerPlayer serverPlayer) {
                clearGrid(serverPlayer);
            }
        }
        if (!world.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            var vm = IXplatAbstractions.INSTANCE.getStaffcastVM(serverPlayer, InteractionHand.MAIN_HAND);
            var patterns = IXplatAbstractions.INSTANCE.getPatternsSavedInUi(serverPlayer);
//            var descs = vm.generateDescs();
//
//            IXplatAbstractions.INSTANCE.sendPacketToPlayer(serverPlayer,
//                    new MsgOpenSpellGuiS2C(hand, patterns, descs.getFirst(), descs.getSecond(),
//                            0)); // TODO: Fix!
        }
    }
}
