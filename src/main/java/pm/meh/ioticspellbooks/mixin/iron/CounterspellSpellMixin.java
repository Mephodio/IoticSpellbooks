package pm.meh.ioticspellbooks.mixin.iron;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.common.msgs.MsgNewSpellPatternS2C;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.spells.ender.CounterspellSpell;
import net.minecraft.network.chat.Component;
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
import pm.meh.ioticspellbooks.compat.hex.mishap.MishapCounterspell;

import java.util.List;

@Mixin(CounterspellSpell.class)
public class CounterspellSpellMixin {
    @Inject(method = "onCast", remap = false,
            at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/util/Utils;serverSideCancelCast(Lnet/minecraft/server/level/ServerPlayer;Z)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    void onCastOnPlayer(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData, CallbackInfo ci,
                        Vec3 localStart, Vec3 localEnd, HitResult localHitResult) {
        if (localHitResult instanceof EntityHitResult entityHitResult &&
                entityHitResult.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(Component.literal("got you"));
            var vm = IXplatAbstractions.INSTANCE.getStaffcastVM(serverPlayer, InteractionHand.MAIN_HAND);
            var image = vm.getImage();
            var stack = image.getStack();
            stack.add(new GarbageIota());

//            vm.performSideEffects(List.of(new OperatorSideEffect.DoMishap(new MishapCounterspell(), new Mishap.Context(null, null))));

            ExecutionClientView clientInfo = new ExecutionClientView(
                false,
                ResolvedPatternType.ERRORED,
                stack.stream().map(IotaType::serialize).toList(),
                image.getUserData().contains(HexAPI.RAVENMIND_USERDATA) ?
                        image.getUserData().getCompound(HexAPI.RAVENMIND_USERDATA) : null
            );
            IXplatAbstractions.INSTANCE.sendPacketToPlayer(serverPlayer,
                    new MsgNewSpellPatternS2C(clientInfo, -1));
        }
    }
}
