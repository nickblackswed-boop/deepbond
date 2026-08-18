package com.nickblack.deepbond;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent;
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
        CobblemonEvents.EVOLUTION_TESTED.subscribe(event -> {
            if (BondData.isActive(event.getPokemon()) && !BondData.isAchieved(event.getPokemon())) {
                event.setResult(false);
            }
            return kotlin.Unit.INSTANCE;
        });

        CobblemonEvents.BATTLE_FAINTED.subscribe(event -> {
            BattleFaintedEvent fainted = event;
            if (fainted.getContext() != null && fainted.getContext().getOrigin() != null) {
                BondData.addDefeat(fainted.getContext().getOrigin().getOriginalPokemon());
            }
        });
    }
}
