package edu.school21.GMBU.emulator.RomInfo;

enum CartridgeTypesEnum
{
	CART_ROM_ONLY  						(0x00),
	CART_ROM_MBC1  						(0x01),
	CART_ROM_MBC1_RAM  					(0x02),
	CART_ROM_MBC1_RAM_BATT  			(0x03),
	CART_ROM_MBC2  						(0x05),
	CART_ROM_MBC2_BATT  				(0x06),
	CART_ROM_RAM  						(0x08),
	CART_ROM_RAM_BATT  					(0x09),
	CART_ROM_MMM01  					(0x0B),
	CART_ROM_MMM01_SRAM  				(0x0C),
	CART_ROM_MMM01_SRAM_BATT  			(0x0D),
	CART_ROM_MBC3_TIMER_BATT  			(0x0F),
	CART_ROM_MBC3_TIMER_RAM_BATT  		(0x10),
	CART_ROM_MBC3  						(0x11),
	CART_ROM_MBC3_RAM  					(0x12),
	CART_ROM_MBC3_RAM_BATT  			(0x13),
	CART_ROM_MBC5  						(0x19),
	CART_ROM_MBC5_RAM  					(0x1A),
	CART_ROM_MBC5_RAM_BATT  			(0x1B),
	CART_ROM_MBC5_RUMBLE  				(0x1C),
	CART_ROM_MBC5_RUMBLE_SRAM  			(0x1D),
	CART_ROM_MBC5_RUMBLE_SRAM_BATT  	(0x1E),
	CART_POCKET_CAMERA  				(0x1F),
	CART_BANDAI_TAMA5  					(0xFD),
	CART_HUDSON_HUC3  					(0xFE),
	CART_HUDSON_HUC1  					(0xFF),
	CART_UNKNOWN  						(0x100);

	private final int cartRomCode;

	CartridgeTypesEnum(int cartRomCode){
		this.cartRomCode = Math.min(cartRomCode, 0x100);
	}
};
