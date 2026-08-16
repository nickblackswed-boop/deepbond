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

        // Friendship is an achievement requirement, not an activation requirement.
        // Activation currently only records that this Pokémon has entered the bond process.
        if (!pokemonEntity.getPokemon().getOwnerPlayerUUID().isPresent()) {
            player.sendMessage(net.minecraft.text.Text.literal("You must own this Pokémon."), true);
            return ActionResult.FAIL;
        }

        player.sendMessage(net.minecraft.text.Text.literal("Deep Bond process activated. Achievement requirements will now be tracked."), true);
        stack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
