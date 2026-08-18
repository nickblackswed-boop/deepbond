package com.nickblack.deepbond;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class DeepBondBallItem extends Item {
    public DeepBondBallItem(Settings settings) { super(settings); }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof PokemonEntity pokemonEntity)) return ActionResult.PASS;
        if (player.getWorld().isClient) return ActionResult.SUCCESS;

        Pokemon pokemon = pokemonEntity.getPokemon();
        if (pokemon.getOwnerPlayer() != player) {
            player.sendMessage(Text.literal("You must own this Pokémon."), true);
            return ActionResult.FAIL;
        }
        if (BondData.isActive(pokemon)) {
            player.sendMessage(Text.literal("This Pokémon is already bonded."), true);
            return ActionResult.FAIL;
        }

        BondData.activate(pokemon);
        String path = BondData.getPath(pokemon);
        player.sendMessage(Text.literal("§6" + path + " Bond activated! §7Achievement progress has started."), true);
        if (!player.isCreative()) stack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
