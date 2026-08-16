package com.nickblack.deepbond;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DeepBondMod implements ModInitializer {
    public static final String MOD_ID = "deepbond";

    public static final Item DEEP_BOND_BALL = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "deep_bond_ball"),
            new DeepBondBallItem(new Item.Settings().maxCount(1))
    );

    @Override
    public void onInitialize() {
        // Once the ball is applied, Cobblemon's evolution test is vetoed until
        // the bond is achieved. This keeps the lock persistent even if the
        // Pokémon is recalled, moved to the PC, or relogged.
        CobblemonEvents.EVOLUTION_TESTED.subscribe(event -> {
            if (BondData.isActive(event.getPokemon())) {
                event.setResult(false);
            }
            return kotlin.Unit.INSTANCE;
        });
    }
}
