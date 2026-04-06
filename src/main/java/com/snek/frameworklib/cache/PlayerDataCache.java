package com.snek.frameworklib.cache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.utils.UtilityClassBase;

import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




/**
 * A class that caches the names and skins of players when they join.
 * <p>
 * This is used access their data when the player is offline.
 * <p>
 * Player cache files are saved instantly whenever a player joins.
 * They are very small and the events are rare enough to consider the overhead is negligible.
 * @since v1.1.0
 */
public final class PlayerDataCache extends UtilityClassBase {



    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<UUID, CachedPlayerData> cache = new HashMap<>();


    /**
     * A class used to store the data of a single player
     */
    private static class CachedPlayerData {
        private final @NotNull String name;
        private final @NotNull String texture;

        public CachedPlayerData(final @NotNull String name, final @NotNull String texture) {
            this.name = name;
            this.texture = texture;
        }

        public @NotNull String getName   () { return name; }
        public @NotNull String getTexture() { return texture; }
    }


    /**
     * Calculates the path to the directory where player data caches are saved.
     * @return The path to the save file directory.
         */
    public static @NotNull Path calcDirPath() {
        return FrameworkLib.getStorageDir(FrameworkLib.LIB_ID).resolve("cache/player data");
    }

    /**
     * Calculates the path to the save file of the specified player.
     * @param playerUuid The uuid of the player.
     * @return The path to the save file of the player's cached data.
         */
    public static @NotNull Path calcFilePath(final @NotNull UUID playerUuid) {
        return calcDirPath().resolve(playerUuid.toString() + ".json");
    }




    /**
     * Creates or overwrites the data of the specified player.
     * <p>
     * This function must be called on ServerPlayConnectionEvents.JOIN event.
     * @param player The player.
     */
    public static void onPlayerJoin(final @NotNull Player player) {

        // Get player UUID and game profile
        final UUID playerUUID = player.getUUID();
        final GameProfile profile = player.getGameProfile();

        // Retrieve texture data
        final var textures = profile.getProperties().get("textures");
        if(textures != null && !textures.isEmpty()) {
            final String textureValue = textures.iterator().next().getValue();
            if(textureValue != null && !textureValue.isBlank()) {
                cache.put(playerUUID, new CachedPlayerData(profile.getName(), textureValue));
                saveCache(playerUUID);
            }
            else {
                //FIXME add proper error logs and fallback logic
            }
        }
        else {
            //FIXME add proper error logs and fallback logic
        }
    }




    /**
     * Loads the data of all the cached players.
     */
    public static void loadAllCaches() {
        try {

            // Create directories
            // For each file in the cache storage directory
            Files.createDirectories(calcDirPath());
            for(final File f : calcDirPath().toFile().listFiles()) {

                // Read the file
                final String json = Files.readString(Path.of(f.getPath()));
                final CachedPlayerData data = GSON.fromJson(json, CachedPlayerData.class);


                // If the file was read successfully, tore its contents in the runtime map
                if(data != null) {
                    final String fileName = f.getName();
                    final UUID playerUUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
                    cache.put(playerUUID, data);
                }

                // If not, log an error and skip the file
                else {
                    //FIXME add proper message and fallback logic
                }
            }
        }
        catch(final IOException e) {
            e.printStackTrace();
            //FIXME add proper message and fallback logic
        }
    }




    /**
     * Retrieves the cached texture of the player with the specified UUID.
     * @param uuid The UUID of the player.
     * @return The texture, or null if not cached.
     */
    public static @Nullable String getPlayerTexture(final @NotNull UUID uuid) {
        return cache.get(uuid).texture;
    }

    /**
     * Retrieves the cached name of the player with the specified UUID.
     * @param uuid The UUID of the player.
     * @return The name, or null if not cached.
     */
    public static @Nullable String getPlayerName(final @NotNull UUID uuid) {
        return cache.get(uuid).name;
    }





    /**
     * Saves the cache of the specified player to its file.
     * @param playerUUID The UUID of the player to save the data cache of.
     */
    private static void saveCache(final @NotNull UUID playerUUID) {
        try {
            Files.writeString(calcFilePath(playerUUID), GSON.toJson(cache.get(playerUUID)));
        }
        catch (final IOException e) {
            e.printStackTrace();
            //FIXME add proper message and fallback logic
        }
    }
}