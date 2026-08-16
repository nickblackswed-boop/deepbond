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
    private static final String ACTIVE_KEY = "deepbond_active";
    private static final String PATH_KEY = "deepbond_path";
    private static final int MAX_FRIENDSHIP = 255;

    public DeepBondBallItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof PokemonEntity pokemonEntity)) {
            return ActionResult.PASS;
        }

        if (player.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();

        if (!pokemon.isPlayerOwned() || !player.getUuid().equals(pokemon.getOwnerUUID())) {
            player.sendMessage(Text.literal("You must own this Pokémon to form a Deep Bond."), true);
            return ActionResult.FAIL;
        }

        if (pokemon.persistentData.getBoolean(ACTIVE_KEY)) {
            player.sendMessage(Text.literal("This Pokémon is already on a Deep Bond path."), true);
            return ActionResult.FAIL;
        }

        if (pokemon.friendship < MAX_FRIENDSHIP) {
            player.sendMessage(Text.literal("This Pokémon needs maximum friendship before bonding can begin."), true);
            return ActionResult.FAIL;
        }

        // The activation point is intentionally simple and persistent.
        // Progress requirements and the final Deep/Eternal determination are added next.
        pokemon.persistentData.putBoolean(ACTIVE_KEY, true);
        pokemon.persistentData.putString(PATH_KEY, "deep");
        pokemon.onChange();

        if (!player.isCreative()) {
            stack.decrement(1);
        }

        player.sendMessage(Text.literal("♥ Deep Bond activated! This Pokémon is now on the Deep Bond path."), true);
        return ActionResult.SUCCESS;
    }
}
