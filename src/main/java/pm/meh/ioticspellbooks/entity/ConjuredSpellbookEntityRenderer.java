package pm.meh.ioticspellbooks.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public class ConjuredSpellbookEntityRenderer extends EntityRenderer<ConjuredSpellbookEntity> {
    public ConjuredSpellbookEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(ConjuredSpellbookEntity p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
