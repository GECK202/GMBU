package edu.school21.GMBU.emulator.RomInfo;

public enum MMCTypesEnum
{
	MMC_ROMONLY     (0x00),
	MMC_MBC1        (0x01),
	MMC_MBC2        (0x02),
	MMC_MBC3        (0x03),
	MMC_MBC5        (0x04),
	MMC_MMM01       (0x05),
	MMC_UNKNOWN     (0x06);

	private final int MMCTypes;

	private MMCTypesEnum(int MMCTypes) {
		this.MMCTypes = Math.min(MMCTypes, 0x06);
	}
};
