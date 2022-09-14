package edu.school21.GMBU.emulator.RomInfo;

enum ROMSizesEnum
{
	ROMSIZE_2BANK   (0x0),
	ROMSIZE_4BANK   (0x1),
	ROMSIZE_8BANK	(0x2),
	ROMSIZE_16BANK  (0x3),
	ROMSIZE_32BANK  (0x4),
	ROMSIZE_64BANK  (0x5),
	ROMSIZE_128BANK (0x6),
	ROMSIZE_72BANK	(0x52),
	ROMSIZE_80BANK	(0x53),
	ROMSIZE_96BANK	(0x54);

	private  final int ROMSizes;

	private ROMSizesEnum(int ROMSizes) {
		this.ROMSizes = ROMSizes;
	}
};
