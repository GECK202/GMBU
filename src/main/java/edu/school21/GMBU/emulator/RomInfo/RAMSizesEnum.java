package edu.school21.GMBU.emulator.RomInfo;

enum RAMSizesEnum
{
	RAMSIZE_NONE		(0x0),
	RAMSIZE_HALFBANK	(0x1),
	RAMSIZE_1BANK		(0x2),
	RAMSIZE_4BANK		(0x3),
	RAMSIZE_16BANK		(0x4);

	private final int RAMSizes;

	private RAMSizesEnum(int RAMSizes) {
		this.RAMSizes = RAMSizes;
	}
};
