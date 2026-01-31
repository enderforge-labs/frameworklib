package com.snek.frameworklib.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;








/**
 * A utility class providing functions to handle network packets and perform related calculations.
 * @since v1.3.0
 */
public final class NetworkUtils extends UtilityClassBase {


    /**
     * Plays a sound on the client of the specified player.
     * <p>
     * Other players won't be able to hear it.
     * @param player The player.
     * @param sound The sound to play.
     * @param volume The sound's volume.
     * @param pitch The sound's pitch.
     */
    public static void playSoundClient(final @NotNull Player player, final @NotNull SoundEvent sound, final float volume, final float pitch) {
        assert Require.nonNull(player, "player");
        assert Require.nonNull(sound, "sound");
        assert Require.nonNegative(volume, "volume");
        assert Require.nonNegative(pitch, "pitch");

        ((ServerPlayer)player).connection.send(
            new ClientboundSoundPacket(
                Holder.direct(sound),
                SoundSource.BLOCKS,
                player.getX(), player.getY(), player.getZ(),
                volume, pitch,
                player.level().getRandom().nextLong()
            )
        );
    }




    /**
     * Sends a slot update to the client of the specified player.
     * @param player The player.
     * @param slot The ID of the slot to update.
     * @param stack The item stack to set.
     */
    public static void sendClientSlotUpdate(final @NotNull Player player, final int slot, final @NotNull ItemStack stack) {
        assert Require.nonNull(player, "player");
        assert Require.nonNegative(slot, "slot");
        assert Require.nonNull(stack, "stack");

        ((ServerPlayer)player).connection.send(new ClientboundContainerSetSlotPacket(
            InventoryMenu.CONTAINER_ID,
            player.containerMenu.getStateId(),
            slot,
            stack
        ));
    }








    /**
     * Calculates the size in bytes of an entity's compressed data.
     * <p>
     * This is a close approximation of what is sent over the network by Minecraft.
     * <p>
     * Notice: This method has to serialize the data and sometimes compress it in order to calculate its size.
     *     This can be very expensive for complex entities.
     *     The returned value should be cached whenever possible.
     * @param entity The entity.
     * @return The size of the entity's compressed data, in bytes. -1 if errors occur.
     */
    public static long calcEntityPayloadSize(final @NotNull Entity entity) {
        final CompoundTag nbt = new CompoundTag();
        entity.saveWithoutId(nbt);
        return calcNbtPayloadSize(nbt);
    }


    /**
     * Calculates the size in bytes of an item's compressed data.
     * <p>
     * This is a close approximation of what is sent over the network by Minecraft.
     * <p>
     * Notice: This method has to serialize the data and sometimes compress it in order to calculate its size.
     *     This can be very expensive for complex entities.
     *     The returned value should be cached whenever possible.
     * @param itemStack The item.
     * @return The size of the item's compressed data, in bytes. -1 if errors occur.
     */
    public static long calcItemPayloadSize(final @NotNull ItemStack itemStack) {
        final CompoundTag nbt = new CompoundTag();
        itemStack.save(nbt);
        return calcNbtPayloadSize(nbt);
    }


    /**
     * Calculates the size in bytes the provided NBT data has when sent over the network.
     * <p>
     * This size is either compressed or uncompressed depending on its uncompressed size.
     * <p>
     * This is a close approximation of what is sent over the network by Minecraft.
     * @param nbt The NBT data.
     * @return The size of the data, in bytes. -1 if errors occur.
     */
    public static long calcNbtPayloadSize(final @NotNull CompoundTag nbt) {
        try {
            final ByteArrayOutputStream uncompressed = new ByteArrayOutputStream();
            final DataOutputStream dataStream = new DataOutputStream(uncompressed);
            nbt.write(dataStream);
            final byte[] data = uncompressed.toByteArray();
            return NetworkUtils.calcNetworkDataSize(data);
        }
        catch (final IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * Calculates the size in bytes the provided data has when sent over the network.
     * <p>
     * This size is either compressed or uncompressed depending on its uncompressed size.
     * <p>
     * This is a close approximation of what is sent over the network by Minecraft.
     * @param data The data as a byte array.
     * @return The size of the data, in bytes.
     */
    public static long calcNetworkDataSize(final @NotNull byte[] data) {

        // Minecraft's default compression threshold
        final int COMPRESSION_THRESHOLD = 256;

        // Return uncompressed size if below threshold, compress and calculate its size otherwise
        return data.length < COMPRESSION_THRESHOLD ?
            getUncompressedDataSize(data) :
            getCompressedDataSize(data)
        ;
    }


    /**
     * Calculates the size in bytes of the provided uncompressed data.
     * @param uncompressedData The uncompressed data.
     * @return The size of the uncompressed data, in bytes.
     */
    public static int getUncompressedDataSize(final @NotNull byte[] uncompressedData) {
        return uncompressedData.length;
    }


    /**
     * Calculates the size in bytes the provided data has when compressed with zlib.
     * @param uncompressedData The uncompressed data.
     * @return The size of the compressed data, in bytes.
     */
    @SuppressWarnings("java:S2093") //! try-with-resources not used
    public static int getCompressedDataSize(final @NotNull byte[] uncompressedData) {
        final Deflater deflater = new Deflater();
        try {
            deflater.setInput(uncompressedData);
            deflater.finish();

            final ByteArrayOutputStream compressed = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                final int count = deflater.deflate(buffer);
                compressed.write(buffer, 0, count);
            }

            return compressed.toByteArray().length;
        }
        finally {
            deflater.end();
        }
    }
}
