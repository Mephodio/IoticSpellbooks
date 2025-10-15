package pm.meh.ioticspellbooks.compat.hex.iface;

import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughMedia;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface SpellActionJava extends SpellAction {
    @Override
    public @NotNull
    default Result executeWithUserdata(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment, @NotNull CompoundTag compoundTag) throws Mishap {
        return execute(list, castingEnvironment);
    }

    @Override
    public @NotNull
    default OperationResult operate(@NotNull CastingEnvironment env, @NotNull CastingImage image, @NotNull SpellContinuation continuation) {
        var stack = new ArrayList<>(image.getStack());
        var argc = getArgc();

        if (argc > stack.size()) {
            throw new MishapNotEnoughArgs(argc, stack.size());
        }

        List<Iota> args = stack.subList(stack.size() - argc, stack.size());
        for (int i = 0; i < argc; i++) {
            stack.remove(stack.size() - 1);
        }

        var userDataMut = image.getUserData().copy();
        var result = executeWithUserdata(args, env, userDataMut);

        var sideEffects = new ArrayList<OperatorSideEffect>();

        if (env.extractMedia(result.getCost(), true) > 0) {
            throw new MishapNotEnoughMedia(result.getCost());
        }
        if (result.getCost() > 0) {
            sideEffects.add(new OperatorSideEffect.ConsumeMedia(result.getCost()));
        }

        sideEffects.add(new OperatorSideEffect.AttemptSpell(
                result.getEffect(),
                hasCastingSound(env),
                awardsCastingStat(env)
        ));

        for (var spray : result.getParticles()) {
            sideEffects.add(new OperatorSideEffect.Particles(spray));
        }

        var image2 = image.copy(
                stack,
                image.getParenCount(),
                image.getParenthesized(),
                image.getEscapeNext(),
                image.getOpsConsumed() + result.getOpCount(),
                userDataMut);

        var sound = hasCastingSound(env) ? HexEvalSounds.SPELL : HexEvalSounds.MUTE;

        return new OperationResult(image2, sideEffects, continuation, sound);
    }
}
