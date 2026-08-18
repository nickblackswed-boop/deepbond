package com.nickblack.deepbond.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.nickblack.deepbond.BondData;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonEntity.class)
public abstract class PokemonEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void deepbond$tick(CallbackInfo ci) {
        PokemonEntity self = (PokemonEntity) (Object) this;
        if (self.getWorld() instanceof ServerWorld serverWorld) {
            BondData.tickOut(self.getPokemon(), serverWorld.getTime() / 24000L);
        }
    }
}
