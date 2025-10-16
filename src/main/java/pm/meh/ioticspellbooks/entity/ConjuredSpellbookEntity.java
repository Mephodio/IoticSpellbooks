package pm.meh.ioticspellbooks.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ConjuredSpellbookEntity extends LivingEntity {
    public ConjuredSpellbookEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {}

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FOLLOW_RANGE, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0);
    }
}
