package edu.school21.GMBU.emulator.MMC;

public class MMC_MBC {

	byte[] ROM;
	byte[] RAMBanks;


	int ROMSize;
	int RAMSize;

	int ROMOffset;
	int RAMOffset;
	boolean RAMEnabled;

	public MMC_MBC(byte[] ROM, int ROMSize, byte[] RAMBanks, int RAMSize) {
		this.ROM = ROM;
		this.ROMSize = ROMSize;
		this.RAMBanks = RAMBanks;
		this.ROMSize = RAMSize;
	}
}
