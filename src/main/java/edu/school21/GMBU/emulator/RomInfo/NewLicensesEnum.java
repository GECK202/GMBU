package edu.school21.GMBU.emulator.RomInfo;

enum NewLicensesEnum
{
	NEWLICENSE_NONE                     (0x00),
	NEWLICENSE_NINTENDO1  				(0x01),
	NEWLICENSE_CAPCOM  					(0x08),
	NEWLICENSE_ELECTRONIC_ARTS1  		(0x13),
	NEWLICENSE_HUDSONSOFT  				(0x18),
	NEWLICENSE_B_AI  					(0x19),
	NEWLICENSE_KSS  					(0x20),
	NEWLICENSE_POW  					(0x22),
	NEWLICENSE_PCM_COMPLETE  			(0x24),
	NEWLICENSE_SAN_X  					(0x25),
	NEWLICENSE_KEMCO_JAPAN  			(0x28),
	NEWLICENSE_SETA  					(0x29),
	NEWLICENSE_VIACOM  					(0x30),
	NEWLICENSE_NINTENDO2  				(0x31),
	NEWLICENSE_BANDIA  					(0x32),
	NEWLICENSE_OCEAN_ACCLAIM1  			(0x33),
	NEWLICENSE_KONAMI1  				(0x34),
	NEWLICENSE_HECTOR  					(0x35),
	NEWLICENSE_TAITO  					(0x37),
	NEWLICENSE_HUDSON  					(0x38),
	NEWLICENSE_BANPRESTO  				(0x39),
	NEWLICENSE_UBISOFT  				(0x41),
	NEWLICENSE_ATLUS  					(0x42),
	NEWLICENSE_MALIBU  					(0x44),
	NEWLICENSE_ANGEL  					(0x46),
	NEWLICENSE_PULLETPROOF  			(0x47),
	NEWLICENSE_IREM  					(0x49),
	NEWLICENSE_ABSOLUTE  				(0x50),
	NEWLICENSE_ACCLAIM  				(0x51),
	NEWLICENSE_ACTIVISION  				(0x52),
	NEWLICENSE_AMERICAN_SUMMY  			(0x53),
	NEWLICENSE_KONAMI2  				(0x54),
	NEWLICENSE_HITECH_ENTERTAINMENT  	(0x55),
	NEWLICENSE_LJN  					(0x56),
	NEWLICENSE_MATCHBOX  				(0x57),
	NEWLICENSE_MATTEL  					(0x58),
	NEWLICENSE_MILTON_BRADLEY  			(0x59),
	NEWLICENSE_TITUS  					(0x60),
	NEWLICENSE_VIRGIN  					(0x61),
	NEWLICENSE_LUCASARTS  				(0x64),
	NEWLICENSE_OCEAN  					(0x67),
	NEWLICENSE_ELECTRONIC_ARTS2  		(0x69),
	NEWLICENSE_INFOGRAMES  				(0x70),
	NEWLICENSE_INTERPLAY  				(0x71),
	NEWLICENSE_BRODERBUND  				(0x72),
	NEWLICENSE_SCULPTURED  				(0x73),
	NEWLICENSE_SCI  					(0x75),
	NEWLICENSE_T_HQ  					(0x78),
	NEWLICENSE_ACCOLADE  				(0x79),
	NEWLICENSE_MISAWA  					(0x80),
	NEWLICENSE_LOZC  					(0x83),
	NEWLICENSE_TOKUMA_SHOTEN_I  		(0x86),
	NEWLICENSE_TSUKUDA_ORI  			(0x87),
	NEWLICENSE_CHUN_SOFT  				(0x91),
	NEWLICENSE_VIDEO_SYSTEM  			(0x92),
	NEWLICENSE_OCEAN_ACCLAIM2  			(0x93),
	NEWLICENSE_VARIE  					(0x95),
	NEWLICENSE_YONEZAWAS_PAL  			(0x96),
	NEWLICENSE_KANEKO  					(0x97),
	NEWLICENSE_PACK_IN_SOFT  			(0x99),
	NEWLICENSE_UNKNOWN  				(0x9A);

	private final int NewLicenses;

	private NewLicensesEnum(int NewLicenses) {
		this.NewLicenses = Math.min(NewLicenses, 0x9A);
	}
};
