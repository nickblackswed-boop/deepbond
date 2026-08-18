package com.nickblack.deepbond;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.NbtCompound;

/** Persistent per-Pokemon Deep/Eternal Bond state. */
public final class BondData {
    private static final String ROOT = "deepbond";
    private static final String ACTIVE = "active";
    private static final String PATH = "path";
    private static final String DEFEATED = "defeated";
    private static final String DAYS_OUT = "days_out";
    private static final String LAST_DAY = "last_day";
    private static final String FRIENDSHIP_DONE = "friendship_done";
    private static final String ACHIEVED = "achieved";

    private BondData() {}

    public static NbtCompound data(Pokemon pokemon) {
        NbtCompound root = pokemon.getPersistentData();
        if (!root.contains(ROOT)) root.put(ROOT, new NbtCompound());
        return root.getCompound(ROOT);
    }

    public static boolean isActive(Pokemon pokemon) { return data(pokemon).getBoolean(ACTIVE); }
    public static String getPath(Pokemon pokemon) { return data(pokemon).getString(PATH); }
    public static int getDefeated(Pokemon pokemon) { return data(pokemon).getInt(DEFEATED); }
    public static int getDaysOut(Pokemon pokemon) { return data(pokemon).getInt(DAYS_OUT); }
    public static boolean hasFriendship(Pokemon pokemon) { return data(pokemon).getBoolean(FRIENDSHIP_DONE); }
    public static boolean isAchieved(Pokemon pokemon) { return data(pokemon).getBoolean(ACHIEVED); }

    public static boolean canBeEternal(Pokemon pokemon) {
        return pokemon.getEvolutions().iterator().hasNext();
    }

    public static void activate(Pokemon pokemon) {
        NbtCompound d = data(pokemon);
        d.putBoolean(ACTIVE, true);
        d.putString(PATH, canBeEternal(pokemon) ? "ETERNAL" : "DEEP");
        d.putInt(DEFEATED, 0);
        d.putInt(DAYS_OUT, 0);
        d.putLong(LAST_DAY, -1L);
        d.putBoolean(FRIENDSHIP_DONE, pokemon.getFriendship() >= 255);
        d.putBoolean(ACHIEVED, false);
        pokemon.onChange();
    }

    public static void tickOut(Pokemon pokemon, long worldDay) {
        if (!isActive(pokemon) || isAchieved(pokemon) || pokemon.getEntity() == null) return;
        NbtCompound d = data(pokemon);
        long last = d.getLong(LAST_DAY);
        if (last < 0L) {
            d.putLong(LAST_DAY, worldDay);
        } else if (worldDay > last) {
            int days = Math.min(50, d.getInt(DAYS_OUT) + (int)(worldDay - last));
            d.putInt(DAYS_OUT, days);
            d.putLong(LAST_DAY, worldDay);
        }
        if (pokemon.getFriendship() >= 255) d.putBoolean(FRIENDSHIP_DONE, true);
        checkAchievement(pokemon);
    }

    public static void addDefeat(Pokemon pokemon) {
        if (!isActive(pokemon) || isAchieved(pokemon)) return;
        NbtCompound d = data(pokemon);
        d.putInt(DEFEATED, Math.min(1000, d.getInt(DEFEATED) + 1));
        checkAchievement(pokemon);
        pokemon.onChange();
    }

    private static void checkAchievement(Pokemon pokemon) {
        NbtCompound d = data(pokemon);
        if (!d.getBoolean(FRIENDSHIP_DONE) && pokemon.getFriendship() >= 255) d.putBoolean(FRIENDSHIP_DONE, true);
        if (d.getBoolean(FRIENDSHIP_DONE) && d.getInt(DEFEATED) >= 1000 && d.getInt(DAYS_OUT) >= 50) {
            if (!d.getBoolean(ACHIEVED)) {
                d.putBoolean(ACHIEVED, true);
                pokemon.onChange();
                if (pokemon.getEntity() != null) {
                    pokemon.getEntity().sendMessage(net.minecraft.text.Text.literal("§6Bond achievement complete! Evolution is unlocked."));
                }
            }
        }
    }
}
