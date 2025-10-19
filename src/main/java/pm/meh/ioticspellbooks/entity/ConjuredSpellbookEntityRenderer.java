package pm.meh.ioticspellbooks.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import pm.meh.ioticspellbooks.IoticSpellbooks;

public class ConjuredSpellbookEntityRenderer
        extends LivingEntityRenderer<ConjuredSpellbookEntity,ConjuredSpellbookModel<ConjuredSpellbookEntity>> {
    public ConjuredSpellbookEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new ConjuredSpellbookModel<>(context.bakeLayer(ConjuredSpellbookModel.LAYER_LOCATION)), 0);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ConjuredSpellbookEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "textures/entity/conjured_spellbook.png");
    }

    @Override
    protected boolean shouldShowName(@NotNull ConjuredSpellbookEntity entity) {
        return super.shouldShowName(entity) && entity.hasCustomName();
    }
}
