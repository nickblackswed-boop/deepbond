package com.nickblack.deepbond;

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
            new Item(new Item.Settings().maxCount(1))
    );

    @Override
    public void onInitialize() {
        // v0.1 foundation. Bond interaction and progression are added next.
    }
}
