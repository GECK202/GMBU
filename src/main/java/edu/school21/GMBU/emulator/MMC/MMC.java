package edu.school21.GMBU.emulator.MMC;

public interface MMC {

	byte read(int address);
	void write(int address, byte value);
}
