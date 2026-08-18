package com.nickblack.deepbond.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.nickblack.deepbond.BondData;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    private static final Identifier SPEED_ID = Identifier.of("deepbond", "trainer_speed");
    private static final Identifier FALL_ID = Identifier.of("deepbond", "trainer_fall");

    @Inject(method = "tick", at = @At("TAIL"))
    private void deepbond$trainerBonuses(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        boolean bondedOut = !player.getWorld().getEntitiesByClass(
                PokemonEntity.class,
                player.getBoundingBox().expand(64.0D),
                e -> e.getPokemon().getOwnerPlayer() == player && BondData.isActive(e.getPokemon())
        ).isEmpty();

        var speed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        var fall = player.getAttributeInstance(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER);
        if (speed == null || fall == null) return;

        if (bondedOut) {
            if (!speed.hasModifier(SPEED_ID)) {
                speed.addTemporaryModifier(new EntityAttributeModifier(SPEED_ID, 0.05D, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (!fall.hasModifier(FALL_ID)) {
                fall.addTemporaryModifier(new EntityAttributeModifier(FALL_ID, -0.20D, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (!player.getWorld().isClient && player.age % 20 == 0 && player.getHealth() < player.getMaxHealth()) {
                player.heal(1.0F);
            }
        } else {
            speed.removeModifier(SPEED_ID);
            fall.removeModifier(FALL_ID);
        }
    }
}
