package pm.meh.ioticspellbooks.compat.hex.iface;

import at.petrak.hexcasting.api.casting.castables.Action;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface ConstMediaActionWithOptionalArgs extends Action {
    int getMinArgc();

    int getCurrentArgc(List<Iota> stack);

    long getMediaCost();

    @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap;

    @Override
    @NotNull
    default OperationResult operate(@NotNull CastingEnvironment env, @NotNull CastingImage image, @NotNull SpellContinuation continuation) {
        var stack = new ArrayList<>(image.getStack());
        var minArgc = getMinArgc();

        if (minArgc > stack.size()) {
            throw new MishapNotEnoughArgs(minArgc, stack.size());
        }

        var currentArgc = getCurrentArgc(stack);

        List<Iota> args = new ArrayList<>();
        for (int i = 0; i < currentArgc; i++) {
            args.add(0, stack.remove(stack.size() - 1));
        }

        var resultStack = this.execute(args, env);
        stack.addAll(resultStack);

        if (env.extractMedia(getMediaCost(), true) > 0) {
            throw new MishapNotEnoughMedia(getMediaCost());
        }

        var sideEffects = new ArrayList<OperatorSideEffect>();
        sideEffects.add(new OperatorSideEffect.ConsumeMedia(getMediaCost()));

        var image2 = image.copy(
                stack,
                image.getParenCount(),
                image.getParenthesized(),
                image.getEscapeNext(),
                image.getOpsConsumed() + 1,
                image.getUserData());

        return new OperationResult(image2, sideEffects, continuation, HexEvalSounds.NORMAL_EXECUTE);
    }
}
