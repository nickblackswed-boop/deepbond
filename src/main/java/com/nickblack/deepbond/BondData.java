package com.nickblack.deepbond;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;

/** Persistent per-Pokemon state for the Deep Bond process. */
public final class BondData {
    private static final String ROOT = "deepbond";
    private static final String ACTIVE = "active";
    private static final String PATH = "path";
    private static final String DEFEATED = "defeated";
    private static final String DAYS_OUT = "days_out";

    private BondData() {}

    private static CompoundTag tag(Pokemon pokemon) {
        CompoundTag root = pokemon.getPersistentData();
        if (!root.contains(ROOT)) root.put(ROOT, new CompoundTag());
        return root.getCompound(ROOT);
    }

    public static boolean isActive(Pokemon pokemon) {
        return tag(pokemon).getBoolean(ACTIVE);
    }

    public static String getPath(Pokemon pokemon) {
        return tag(pokemon).getString(PATH);
    }

    public static int getDefeated(Pokemon pokemon) {
        return tag(pokemon).getInt(DEFEATED);
    }

    public static int getDaysOut(Pokemon pokemon) {
        return tag(pokemon).getInt(DAYS_OUT);
    }

    public static void activate(Pokemon pokemon, String path) {
        CompoundTag data = tag(pokemon);
        data.putBoolean(ACTIVE, true);
        data.putString(PATH, path);
        data.putInt(DEFEATED, 0);
        data.putInt(DAYS_OUT, 0);
        pokemon.onChange();
    }
}
