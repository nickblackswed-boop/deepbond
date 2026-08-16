package com.nickblack.deepbond;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class DeepBondBallItem extends Item {
    public DeepBondBallItem(Settings settings) { super(settings); }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof PokemonEntity pokemonEntity)) return ActionResult.PASS;
        if (player.getWorld().isClient) return ActionResult.SUCCESS;

        var pokemon = pokemonEntity.getPokemon();
        if (pokemon.getOwnerPlayer() != player) {
            player.sendMessage(net.minecraft.text.Text.literal("You must own this Pokémon."), true);
            return ActionResult.FAIL;
        }
        if (BondData.isActive(pokemon)) {
            player.sendMessage(net.minecraft.text.Text.literal("This Pokémon is already on a bond path."), true);
            return ActionResult.FAIL;
        }

        // Path eligibility is deliberately kept separate from achievement progress.
        // The final Deep/Eternal starting requirements will select the path here.
        BondData.activate(pokemon, "DEEP");
        player.sendMessage(net.minecraft.text.Text.literal("Deep Bond path activated. Achievement progress has started."), true);
        if (!player.isCreative()) stack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
