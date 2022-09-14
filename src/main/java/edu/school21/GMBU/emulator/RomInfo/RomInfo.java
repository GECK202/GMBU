package edu.school21.GMBU.emulator.RomInfo;


import org.springframework.stereotype.Component;

/*
An internal information area is located at 0100-014F in each cartridge. It contains the following values:
        0100-0103	This is the begin code execution point in a cart. Usually there is a NOP and
                    a JP instruction here but not always.
        0104-0133	Scrolling Nintendo graphic:
                    CE ED 66 66 CC 0D 00 0B 03 73 00 83 00 0C 00 0D
                    00 08 11 1F 88 89 00 0E DC CC 6E E6 DD DD D9 99
                    BB BB 67 63 6E 0E EC CC DD DC 99 9F BB B9 33 3E
                    ( PROGRAM WON'T RUN IF CHANGED!!!)
        0134-0142	Title of the game in UPPER CASE ASCII. If it is less than 16 characters then
                    the remaining bytes are filled with 00's.
        0143		$80 = Color GB, $00 or other = not Color GB
        0144		Ascii hex digit, high nibble of licensee code (new).
        0145		Ascii hex digit, low nibble of licensee code (new).
        0146		GB/SGB Indicator (00 = GameBoy, 03 = Super GameBoy functions)
                    (Super GameBoy functions won't work	if <> $03.)
        0147		Cartridge type:
                    0-ROM ONLY				12-ROM+MBC3+RAM
                    1-ROM+MBC1				13-ROM+MBC3+RAM+BATT
                    2-ROM+MBC1+RAM			19-ROM+MBC5
                    3-ROM+MBC1+RAM+BATT		1A-ROM+MBC5+RAM
                    5-ROM+MBC2				1B-ROM+MBC5+RAM+BATT
                    6-ROM+MBC2+BATTERY		1C-ROM+MBC5+RUMBLE
                    8-ROM+RAM				1D-ROM+MBC5+RUMBLE+SRAM
                    9-ROM+RAM+BATTERY		1E-ROM+MBC5+RUMBLE+SRAM+BATT
                    B-ROM+MMM01				1F-Pocket Camera
                    C-ROM+MMM01+SRAM		FD-Bandai TAMA5
                    D-ROM+MMM01+SRAM+BATT	FE - Hudson HuC-3
                    F-ROM+MBC3+TIMER+BATT	FF - Hudson HuC-1
                    10-ROM+MBC3+TIMER+RAM+BATT
                    11-ROM+MBC3
        0148		ROM size:
                    0 - 256Kbit = 32KByte = 2 banks
                    1 - 512Kbit = 64KByte = 4 banks
                    2 - 1Mbit = 128KByte = 8 banks
                    3 - 2Mbit = 256KByte = 16 banks
                    4 - 4Mbit = 512KByte = 32 banks
                    5 - 8Mbit = 1MByte = 64 banks
                    6 - 16Mbit = 2MByte = 128 banks
                    $52 - 9Mbit = 1.1MByte = 72 banks
                    $53 - 10Mbit = 1.2MByte = 80 banks
                    $54 - 12Mbit = 1.5MByte = 96 banks
        0149		RAM size:
                    0 - None
                    1 - 16kBit = 2kB = 1 bank
                    2 - 64kBit = 8kB = 1 bank
                    3 - 256kBit = 32kB = 4 banks
                    4 - 1MBit =128kB =16 banks
        014A		Destination code:
                    0 - Japanese
                    1 - Non-Japanese
        014B		Licensee code (old):
                    33 - Check 0144/0145 for Licensee code.
                    79 - Accolade
                    A4 - Konami
                    (Super GameBoy function won't work if <> $33.)
        014C		Mask ROM Version number (Usually $00)
        014D		Complement check
                    (PROGRAM WON'T RUN ON GB IF NOT CORRECT!!!)
                    (It will run on Super GB, however, if incorrect.)
        014E-014F	Checksum (higher byte first) produced by adding all bytes of a cartridge except for two
                    checksum bytes and taking two lower bytes of the result. (GameBoy ignores this value.)
*/
@Component
public class RomInfo {

	public int[] NintendoGraphics = {
			0xCE, 0xED, 0x66, 0x66, 0xCC, 0x0D, 0x00, 0x0B, 0x03, 0x73, 0x00, 0x83,
			0x00, 0x0C, 0x00, 0x0d, 0x00, 0x08, 0x11, 0x1F, 0x88, 0x89, 0x00, 0x0E,
			0xDC, 0xCC, 0x6E, 0xE6, 0xDD, 0xDD, 0xD9, 0x99, 0xBB, 0xBB, 0x67, 0x63,
			0x6E, 0x0E, 0xEC, 0xCC, 0xDD, 0xDC, 0x99, 0x9F, 0xBB, 0xB9, 0x33, 0x3E
	};

	public String[] CartridgeTypes = new String[0x101];	//Cartridge type # -> Cartridge type string table
	public String[] NewLicenses = new String[0x9B];		//New license # -> New license string table
	public String[] OldLicenses = new String[0x101];		//Old license # -> Old license string table
	public String[] DestinationCodes = new String[3];		//Destination # -> Destination string table
	public int[] ROMSizes = new int[0xFF];					//ROM size # -> ROM size value table
	public int[] RAMSizes = new int[0xFF];					//RAM size # -> RAM size value table

	{
		for (int i = 0; i < 0x101; i++) {
			CartridgeTypes[i] = "Unknown";
		}
		CartridgeTypes[0x00] = "ROM ONLY";
		CartridgeTypes[0x01] = "ROM+MBC1";
		CartridgeTypes[0x02] = "ROM+MBC1+RAM";
		CartridgeTypes[0x03] = "ROM+MBC1+RAM+BATT";
		CartridgeTypes[0x05] = "ROM+MBC2";
		CartridgeTypes[0x06] = "ROM+MBC2+BATT";
		CartridgeTypes[0x08] = "ROM+RAM";
		CartridgeTypes[0x09] = "ROM+RAM+BATT";
		CartridgeTypes[0x0B] = "ROM+MMM01";
		CartridgeTypes[0x0C] = "ROM+MMM01+SRAM";
		CartridgeTypes[0x0D] = "ROM+MMM01+SRAM+BATT";
		CartridgeTypes[0x0F] = "ROM+MBC3+TIMER+BATT";
		CartridgeTypes[0x10] = "ROM+MBC3+TIMER+RAM+BATT";
		CartridgeTypes[0x11] = "ROM+MBC3";
		CartridgeTypes[0x12] = "ROM+MBC3+RAM";
		CartridgeTypes[0x13] = "ROM+MBC3+RAM+BATT";
		CartridgeTypes[0x19] = "ROM+MBC5";
		CartridgeTypes[0x1A] = "ROM+MBC5+RAM";
		CartridgeTypes[0x1B] = "ROM+MBC5+RAM+BATT";
		CartridgeTypes[0x1C] = "ROM+MBC5+RUMBLE";
		CartridgeTypes[0x1D] = "ROM+MBC5+RUMBLE+SRAM";
		CartridgeTypes[0x1E] = "ROM+MBC5+RUMBLE+SRAM+BATT";
		CartridgeTypes[0x1F] = "Pocket Camera";
		CartridgeTypes[0xFD] = "Bandai TAMA5";
		CartridgeTypes[0xFE] = "Hudson HuC-3";
		CartridgeTypes[0xFF] = "Hudson HuC-1";

		DestinationCodes[0] = "Japanese";
		DestinationCodes[1] = "Non-Japanese";
		DestinationCodes[2] = "Unknown";

		for (int i = 0; i < 0x9B; i++) {
			NewLicenses[i] = "Unknown";
		}
		NewLicenses[0x00] = "None";
		NewLicenses[0x01] = "Nintendo";
		NewLicenses[0x31] = "Nintendo";
		NewLicenses[0x08] = "Capcom";
		NewLicenses[0x13] = "Electronic Arts";
		NewLicenses[0x69] = "Electronic Arts";
		NewLicenses[0x18] = "HudsonSoft";
		NewLicenses[0x19] = "B-AI";
		NewLicenses[0x20] = "KSS";
		NewLicenses[0x22] = "POW";
		NewLicenses[0x24] = "PCM Complete";
		NewLicenses[0x25] = "San-X";
		NewLicenses[0x28] = "Kemco Japan";
		NewLicenses[0x29] = "Seta";
		NewLicenses[0x30] = "Viacom";
		NewLicenses[0x32] = "Bandia";
		NewLicenses[0x33] = "ocean/acclaim";
		NewLicenses[0x93] = "ocean/acclaim";
		NewLicenses[0x34] = "Konami";
		NewLicenses[0x54] = "Konami";
		NewLicenses[0x35] = "Hector";
		NewLicenses[0x37] = "Taito";
		NewLicenses[0x38] = "Hudson";
		NewLicenses[0x39] = "Banpresto";
		NewLicenses[0x41] = "Ubisoft";
		NewLicenses[0x42] = "Atlus";
		NewLicenses[0x44] = "Malibu";
		NewLicenses[0x46] = "Angel";
		NewLicenses[0x47] = "Pullet-Proof";
		NewLicenses[0x49] = "Irem";
		NewLicenses[0x50] = "Absolute";
		NewLicenses[0x51] = "Acclaim";
		NewLicenses[0x52] = "Activision";
		NewLicenses[0x53] = "American Summy";
		NewLicenses[0x55] = "HiTech Entertainment";
		NewLicenses[0x56] = "LJN";
		NewLicenses[0x57] = "Matchbox";
		NewLicenses[0x58] = "Mattel";
		NewLicenses[0x59] = "Milton Bradley";
		NewLicenses[0x60] = "Titus";
		NewLicenses[0x61] = "Virgin";
		NewLicenses[0x64] = "Lucas Arts";
		NewLicenses[0x67] = "Ocean";
		NewLicenses[0x70] = "Infogrames";
		NewLicenses[0x71] = "Interplay";
		NewLicenses[0x72] = "Broderbund";
		NewLicenses[0x73] = "Sculptured";
		NewLicenses[0x75] = "SCI";
		NewLicenses[0x78] = "T*HQ";
		NewLicenses[0x79] = "Accolade";
		NewLicenses[0x80] = "Misawa";
		NewLicenses[0x83] = "Lozc";
		NewLicenses[0x86] = "Tokuma Shoten I";
		NewLicenses[0x87] = "Tsukuda Ori";
		NewLicenses[0x91] = "Chun Soft";
		NewLicenses[0x92] = "Video System";
		NewLicenses[0x95] = "Varie";
		NewLicenses[0x96] = "Yonezawas PAL";
		NewLicenses[0x97] = "Kaneko";
		NewLicenses[0x99] = "Pack in Soft";

		for (int i = 0; i < 0x101; i++) {
			OldLicenses[i] = "Unknown";
		}
		OldLicenses[0x00] = "None";
		OldLicenses[0x01] = "Nintendo";
		OldLicenses[0x31] = "Nintendo";
		OldLicenses[0x0A] = "Jaleco";
		OldLicenses[0xE0] = "Jaleco";
		OldLicenses[0x13] = "ElectronicArts";
		OldLicenses[0x69] = "ElectronicArts";
		OldLicenses[0x1A] = "Yanoman";
		OldLicenses[0x24] = "PCMComplete";
		OldLicenses[0x29] = "Seta";
		OldLicenses[0x32] = "Bandai";
		OldLicenses[0xA2] = "Bandai";
		OldLicenses[0xB2] = "Bandai";
		OldLicenses[0x35] = "Hector";
		OldLicenses[0x3C] = "EntertainmentI";
		OldLicenses[0x42] = "Atlus";
		OldLicenses[0xEB] = "Atlus";
		OldLicenses[0x47] = "SpectrumHoloby";
		OldLicenses[0x44] = "Malibu";
		OldLicenses[0x4D] = "Malibu";
		OldLicenses[0x51] = "Acclaim";
		OldLicenses[0xB0] = "Accalim";
		OldLicenses[0x54] = "Gametek";
		OldLicenses[0x57] = "MatchBox";
		OldLicenses[0x5B] = "Romstar";
		OldLicenses[0x60] = "Titus";
		OldLicenses[0x70] = "Infogrames";
		OldLicenses[0x30] = "Infogrames";
		OldLicenses[0x73] = "SculpteredSoft";
		OldLicenses[0x79] = "Accolade";
		OldLicenses[0x7F] = "Kemco";
		OldLicenses[0xC2] = "Kemco";
		OldLicenses[0x86] = "TokumaShotenI";
		OldLicenses[0xC4] = "TokumaShotenI";
		OldLicenses[0x8E] = "APE";
		OldLicenses[0x92] = "VideoSystem";
		OldLicenses[0x96] = "YonezawasPAL";
		OldLicenses[0x9A] = "NihonBussan";
		OldLicenses[0x9D] = "Banpresto";
		OldLicenses[0x39] = "Banpresto";
		OldLicenses[0xD9] = "Banpresto";
		OldLicenses[0xA7] = "Takara";
		OldLicenses[0xAC] = "ToeiAnimation";
		OldLicenses[0xB4] = "Enix";
		OldLicenses[0xB9] = "PonyCanyon";
		OldLicenses[0xBD] = "Sony/ImageSoft";
		OldLicenses[0xC5] = "DataEast";
		OldLicenses[0xC9] = "UFL";
		OldLicenses[0xCC] = "Use";
		OldLicenses[0xCF] = "Angel";
		OldLicenses[0x46] = "Angel";
		OldLicenses[0xD2] = "Quest";
		OldLicenses[0xD6] = "NaxatSoft";
		OldLicenses[0x5C] = "NaxatSoft";
		OldLicenses[0xDA] = "Tomy";
		OldLicenses[0xDE] = "Human";
		OldLicenses[0xE1] = "Towachiki";
		OldLicenses[0xE5] = "Epoch";
		OldLicenses[0xE9] = "Natsume";
		OldLicenses[0xEC] = "EpicSonyRecords";
		OldLicenses[0xF3] = "ExteremeEntertainment";
		OldLicenses[0x08] = "Capcom";
		OldLicenses[0x38] = "Capcom";
		OldLicenses[0x0B] = "Coconuts";
		OldLicenses[0x18] = "Hudsonsoft";
		OldLicenses[0x1D] = "Clary";
		OldLicenses[0x25] = "SanX";
		OldLicenses[0x33] = "Seeabove";
		OldLicenses[0x3E] = "Gremlin";
		OldLicenses[0x49] = "Irem";
		OldLicenses[0x4F] = "USGold";
		OldLicenses[0x52] = "Activision";
		OldLicenses[0x55] = "ParkPlace";
		OldLicenses[0x59] = "MiltonBradley";
		OldLicenses[0x61] = "Virgin";
		OldLicenses[0x1F] = "Virgin";
		OldLicenses[0x4A] = "Virgin";
		OldLicenses[0x6E] = "EliteSystems";
		OldLicenses[0x0C] = "EliteSystems";
		OldLicenses[0x71] = "Interplay";
		OldLicenses[0x75] = "TheSalesCurve";
		OldLicenses[0x7A] = "TriffixEntertainment";
		OldLicenses[0x80] = "MisawaEntertainment";
		OldLicenses[0x8B] = "Bullet-ProofSoftware";
		OldLicenses[0x8F] = "IMAX";
		OldLicenses[0x93] = "Tsuburava";
		OldLicenses[0x97] = "Kaneko";
		OldLicenses[0x9B] = "Tecmo";
		OldLicenses[0x9F] = "Nova";
		OldLicenses[0xA4] = "Konami";
		OldLicenses[0x34] = "Konami";
		OldLicenses[0xA9] = "TechnosJAPAN";
		OldLicenses[0xAD] = "Toho";
		OldLicenses[0xB1] = "ASCIIOrNexoft";
		OldLicenses[0xB6] = "HAL";
		OldLicenses[0xBA] = "CultureBrainO";
		OldLicenses[0xBF] = "Sammy";
		OldLicenses[0xC3] = "SquareSoft";
		OldLicenses[0xC6] = "TonkinHouse";
		OldLicenses[0xCA] = "Ultra";
		OldLicenses[0xCD] = "Meldac";
		OldLicenses[0xD0] = "Taito";
		OldLicenses[0xC0] = "Taito";
		OldLicenses[0xD3] = "SigmaEnterprises";
		OldLicenses[0xD7] = "CopyaSystems";
		OldLicenses[0xDB] = "LJN";
		OldLicenses[0xFF] = "LJN";
		OldLicenses[0x56] = "LJN";
		OldLicenses[0xDF] = "Altron";
		OldLicenses[0xE2] = "Uutaka";
		OldLicenses[0xE7] = "Athena";
		OldLicenses[0xEA] = "KingRecords";
		OldLicenses[0xEE] = "IGS";
		OldLicenses[0x09] = "HotB";
		OldLicenses[0x19] = "ITCEntertainment";
		OldLicenses[0x28] = "KotobukiSystems";
		OldLicenses[0x41] = "Ubisoft";
		OldLicenses[0x50] = "Absolute";
		OldLicenses[0x53] = "AmericanSammy";
		OldLicenses[0x5A] = "Mindscape";
		OldLicenses[0x5D] = "Tradewest";
		OldLicenses[0x67] = "Ocean";
		OldLicenses[0x6F] = "Electro-Brain";
		OldLicenses[0x72] = "Broderbund";
		OldLicenses[0xAA] = "Broderbund";
		OldLicenses[0x78] = "THQ";
		OldLicenses[0x7C] = "Microprose";
		OldLicenses[0x83] = "Lozc";
		OldLicenses[0x8C] = "Victokai";
		OldLicenses[0x91] = "Chunsoft";
		OldLicenses[0x95] = "Varie";
		OldLicenses[0xE3] = "Varie";
		OldLicenses[0x99] = "Arc";
		OldLicenses[0x9C] = "Imagineer";
		OldLicenses[0xA1] = "HoriElectric";
		OldLicenses[0xA6] = "Kawada";
		OldLicenses[0xAF] = "Namco";
		OldLicenses[0xB7] = "SNK";
		OldLicenses[0xBB] = "Sunsoft";
		OldLicenses[0xC8] = "Koei";
		OldLicenses[0xCB] = "Vap";
		OldLicenses[0xCE] = "PonyCanyonOR";
		OldLicenses[0xD1] = "Sofel";
		OldLicenses[0xD4] = "AskKodansha";
		OldLicenses[0xDD] = "NCS";
		OldLicenses[0xE8] = "Asmik";
		OldLicenses[0xF0] = "Awave";

		ROMSizes[0] = 2;
		ROMSizes[1] = 4;
		ROMSizes[2] = 8;
		ROMSizes[3] = 16;
		ROMSizes[4] = 32;
		ROMSizes[5] = 64;
		ROMSizes[6] = 128;
		ROMSizes[0x52] = 72;
		ROMSizes[0x53] = 80;
		ROMSizes[0x54] = 96;

		RAMSizes[0] = 0;
		RAMSizes[1] = 1;
		RAMSizes[2] = 1;
		RAMSizes[3] = 4;
		RAMSizes[4] = 16;
	}
}
