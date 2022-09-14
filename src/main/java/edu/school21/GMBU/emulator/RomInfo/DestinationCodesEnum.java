package edu.school21.GMBU.emulator.RomInfo;

enum DestinationCodesEnum
{
	DESTINATIONCODE_JAPANESE		(0x0),
	DESTINATIONCODE_NONJAPANESE		(0x1),
	DESTINATIONCODE_UNKNOWN			(0x2);

	private final int DestinationCodes;

	private DestinationCodesEnum(int DestinationCodes) {
		this.DestinationCodes = DestinationCodes;
	}
};
