package pm.meh.ioticspellbooks.compat.hex;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.HexRegistries;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import pm.meh.ioticspellbooks.IoticSpellbooks;
import pm.meh.ioticspellbooks.compat.hex.action.OpCastIronSpell;

public class HexRegistry {
    public void register(RegisterEvent event) {
        if (event.getRegistryKey() == HexRegistries.ACTION) {
            registerActions();
        }
    }

    private void registerActions() {
        HexPattern pattern = HexPattern.fromAngles("aaeedqeqaqdeewwdwwwwewwdwww", HexDir.WEST);

        Registry.register(HexActions.REGISTRY,
                ResourceLocation.fromNamespaceAndPath(IoticSpellbooks.MODID, "cast_iron_spell"),
                new ActionRegistryEntry(pattern, new OpCastIronSpell()));
    }
}
