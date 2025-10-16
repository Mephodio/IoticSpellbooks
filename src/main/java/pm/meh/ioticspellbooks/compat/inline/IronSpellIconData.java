package pm.meh.ioticspellbooks.compat.inline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.samsthenerd.inline.Inline;
import com.samsthenerd.inline.api.data.SpriteInlineData;
import com.samsthenerd.inline.utils.Spritelike;
import com.samsthenerd.inline.utils.TextureSprite;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.resources.ResourceLocation;

public class IronSpellIconData extends SpriteInlineData {

    public ResourceLocation spellId;

    public IronSpellIconData(ResourceLocation spellId) {
        super(spriteFromSpellId(spellId));
    }

    public static Spritelike spriteFromSpellId(ResourceLocation spellId) {
        ResourceLocation iconResource = SpellRegistry.getSpell(spellId).getSpellIconResource();
        return new TextureSprite(iconResource);
    }

    public static class IronSpellIconDataType extends SpriteDataType {
        public static IronSpellIconData.IronSpellIconDataType INSTANCE = new IronSpellIconData.IronSpellIconDataType();

        @Override
        public ResourceLocation getId(){
            return ResourceLocation.fromNamespaceAndPath(Inline.MOD_ID, "modicon");
        }

        @Override
        public Codec<SpriteInlineData> getCodec(){
            return ResourceLocation.CODEC.flatComapMap(
                    IronSpellIconData::new,
                    (SpriteInlineData data) -> {
                        if(!(data instanceof IronSpellIconData mData)) {
                            return DataResult.error(() -> "");
                        }
                        return DataResult.success(mData.spellId);
                    }
            );
        }
    }
}
