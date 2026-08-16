package com.nickblack.deepbond

import net.fabricmc.api.ModInitializer
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object DeepBondMod : ModInitializer {
    const val MOD_ID = "deepbond"

    val DEEP_BOND_BALL: Item = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "deep_bond_ball"),
        Item(Item.Settings().maxCount(1))
    )

    override fun onInitialize() {
        println("[Deep Bond] Initialized")
    }
}
