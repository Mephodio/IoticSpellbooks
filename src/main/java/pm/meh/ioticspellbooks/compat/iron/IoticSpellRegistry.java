package pm.meh.ioticspellbooks.compat.iron;

import at.petrak.hexcasting.common.lib.HexSounds;
import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
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

    public static final ResourceLocation HEX_SCHOOL_RESOURCE = ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "hex");
    public static final TagKey<Item> HEX_SCHOOL_FOCUS = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "hex_focus"));
    public static final ResourceKey<DamageType> HEX_SCHOOL_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "hex_magic"));
    public static final RegistryObject<SchoolType> HEX_SCHOOL = registerSchool(new SchoolType(
            HEX_SCHOOL_RESOURCE,
            HEX_SCHOOL_FOCUS,
            Component.translatable("school.ioticspellbooks.hex").withStyle(ChatFormatting.DARK_PURPLE),
            LazyOptional.of(IoticSpellbooks.HEX_SPELL_POWER::get),
            LazyOptional.of(IoticSpellbooks.HEX_MAGIC_RESIST::get),
            LazyOptional.of(() -> HexSounds.CAST_NORMAL),
            HEX_SCHOOL_DAMAGE_TYPE
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

    public static RegistryObject<Attribute> newResistanceAttribute(String id) {
        return IoticSpellbooks.ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.ioticspellbooks." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
    }

    public static RegistryObject<Attribute> newPowerAttribute(String id) {
        return IoticSpellbooks.ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.ioticspellbooks." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
    }
}