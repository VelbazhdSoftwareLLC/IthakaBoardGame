package eu.veldsoft.ithaka.board.game.model;

import java.util.Random;
import java.util.UUID;

/**
 * Common used utilities.
 *
 * @author Todor Balabanov
 */
public class Util {
    /**
     * Pseud-random number generator.
     */
    public static final Random PRNG = new Random();

    /**
     * Bluetooth communication unique identifier.
     */
    public static final UUID BLUETOOTH_UUID = UUID.fromString("7cc45650-6e55-4617-b502-001c31c757ba");

    /**
     * Bluetooth communication name.
     */
    public static final String BLUETOOTH_NAME = "eu.veldsoft.ithaka.board.game";
}
