package pm.meh.ioticspellbooks.compat.iron;

import at.petrak.hexcasting.common.lib.HexSounds;
import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.compat.iron.spell.HexGridSpell;

public class IoticSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, IoticSpellbooks.MODID);
    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, IoticSpellbooks.MODID);

    public static final RegistryObject<AbstractSpell> HEX_GRID_SPELL = registerSpell(new HexGridSpell());

    public static final TagKey<Item> HEX_SCHOOL_FOCUS = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "hex_focus"));
    public static final RegistryObject<SchoolType> HEX_SCHOOL = registerSchool(new SchoolType(
            IoticSpellbooks.HEX_SCHOOL_RESOURCE,
            HEX_SCHOOL_FOCUS,
            Component.translatable("school.ioticspellbooks.hex").withStyle(ChatFormatting.DARK_PURPLE),
            LazyOptional.of(AttributeRegistry.ELDRITCH_SPELL_POWER::get),
            LazyOptional.of(AttributeRegistry.ELDRITCH_MAGIC_RESIST::get),
            LazyOptional.of(() -> HexSounds.CAST_NORMAL),
            ISSDamageTypes.ELDRITCH_MAGIC
    ));

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
        SCHOOLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }
}