package pm.meh.ioticspellbooks.mixin.iron;

import at.petrak.hexcasting.common.msgs.MsgClearSpiralPatternsS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.spells.ender.CounterspellSpell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.meh.ioticspellbooks.compat.hex.msg.MsgCloseSpellGuiS2C;
import pm.meh.ioticspellbooks.compat.hex.util.StaffGridUtils;
import pm.meh.ioticspellbooks.network.PacketHandler;

@Mixin(CounterspellSpell.class)
public class CounterspellSpellMixin {
    @Inject(method = "onCast", remap = false,
            at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/util/Utils;serverSideCancelCast(Lnet/minecraft/server/level/ServerPlayer;Z)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    void onCastOnPlayer(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData, CallbackInfo ci,
                        Vec3 localStart, Vec3 localEnd, HitResult localHitResult) {
        if (!world.isClientSide
                && localHitResult instanceof EntityHitResult entityHitResult
                && entityHitResult.getEntity() instanceof ServerPlayer serverPlayer) {
            StaffGridUtils.clearGrid(serverPlayer);
            StaffGridUtils.closeGrid(serverPlayer);
        }
    }
}
