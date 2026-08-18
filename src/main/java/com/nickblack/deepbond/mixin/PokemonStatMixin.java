package com.nickblack.deepbond.mixin;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.nickblack.deepbond.BondData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pokemon.class)
public abstract class PokemonStatMixin {
    @Inject(method = "getStat", at = @At("RETURN"), cancellable = true)
    private void deepbond$boostStat(Stat stat, CallbackInfoReturnable<Integer> cir) {
        Pokemon pokemon = (Pokemon) (Object) this;
        if (!BondData.isActive(pokemon) || pokemon.getEntity() == null) return;
        if (!(stat == Stats.HP || stat == Stats.ATTACK || stat == Stats.DEFENCE ||
                stat == Stats.SPECIAL_ATTACK || stat == Stats.SPECIAL_DEFENCE || stat == Stats.SPEED)) return;
        double multiplier = "ETERNAL".equals(BondData.getPath(pokemon)) ? 1.10D : 1.05D;
        cir.setReturnValue((int) Math.floor(cir.getReturnValue() * multiplier));
    }
}
