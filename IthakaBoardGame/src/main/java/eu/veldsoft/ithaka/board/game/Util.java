package eu.veldsoft.ithaka.board.game;

import java.util.Random;
import java.util.UUID;

/**
 * Common used utilities.
 * 
 * @author Todor Balabanov
 */
class Util {
	/**
	 * Pseud-random number generator.
	 */
	protected static final Random PRNG = new Random();

	/**
	 * Bluetooth communication unique identifier.
	 */
	protected static final UUID BLUETOOTH_UUID = UUID.fromString("7cc45650-6e55-4617-b502-001c31c757ba");

	/**
	 * Bluetooth communication name.
	 */
	protected static final String BLUETOOTH_NAME = "eu.veldsoft.ithaka.board.game";
}
