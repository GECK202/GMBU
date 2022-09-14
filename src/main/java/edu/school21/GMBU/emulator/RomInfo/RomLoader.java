package edu.school21.GMBU.emulator.RomInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class RomLoader{

	public char[] ROMFile = new char[255];
	public char[] gameTitle = new char[17];
	public boolean colorGB;
	public boolean superGB;
	public CartridgeTypesEnum cartType;
	public DestinationCodesEnum destinationCode;
	public MMCTypesEnum MMCType = MMCTypesEnum.MMC_MBC1;
	public int BatterySupport;
	public int RumbleSupport;
	public int RTCSupport;
	public String newLicense;
	public String oldLicense;
	public int ROMSize;
	public int RAMSize;

	@Autowired
	private RomInfo romInfo;

	//private byte[] ROM;

	private boolean checkRom(byte[] ROM) {
		for (int i = 0; i < 48; i++) {
			if ((byte) romInfo.NintendoGraphics[i] != ROM[i + 0x104]) {
				System.err.printf("Nintendo graphics not equal in %d byte!\n", i);
				System.exit(1);
			}
		}
		byte Complement = 0;
		for (int i = 0x134; i < 0x14D; i++) {
			byte b = ROM[i];
			Complement -= b;
			Complement -= 1;
		}
		if (Complement != ROM[0x14D]) {
			System.err.println("Invalid header checksum!");
			System.exit(1);
		}
		System.out.println("Nintendo graphics is OK!");
		return true;
	}

	public boolean loadRom(byte[] ROM, String fileName) {
		try (FileInputStream fin = new FileInputStream(fileName)) {
			ROM = new byte[fin.available()];
			int count = fin.read(ROM, 0, fin.available());
			if (count > 0) {
				if (checkRom(ROM)) {
					setRomInfo(ROM);
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	public byte getByte(int index) {
		return ROM[index];
	}
	// */

	//public int getMaxAddress() {return  ROM.length;}

	public void setRomInfo(byte[] ROM) {
		colorGB = (ROM[0x143] == (byte)0x80) || (ROM[0x143] == (byte)0xC0);
		superGB = (ROM[0x146] == (byte)0x03);
		ROMSize = romInfo.ROMSizes[ROM[0x148]];
		RAMSize = romInfo.RAMSizes[ROM[0x149]];

		newLicense = romInfo.NewLicenses[(ROM[0x144] & 0xF0) | (ROM[0x145] & 0x0F)];

		//(ROM[0x147]);
		cartType = CartridgeTypesEnum.values()[ROM[0x147]];

		destinationCode = DestinationCodesEnum.values()[ROM[0x14A]];

		oldLicense = romInfo.OldLicenses[ROM[0x14B]];

		switch(cartType)
		{
			case CART_ROM_ONLY:
			case CART_ROM_RAM:
				MMCType = MMCTypesEnum.MMC_ROMONLY;
				BatterySupport = 0;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;

			case CART_ROM_MBC1:
			case CART_ROM_MBC1_RAM:
			case CART_HUDSON_HUC3:
			case CART_HUDSON_HUC1:
				MMCType = MMCTypesEnum.MMC_MBC1;
				BatterySupport = 0;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_MBC1_RAM_BATT:
				MMCType = MMCTypesEnum.MMC_MBC1;
				BatterySupport = 1;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;

			case CART_ROM_MBC2:
				MMCType = MMCTypesEnum.MMC_MBC2;
				BatterySupport = 0;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_MBC2_BATT:
				MMCType = MMCTypesEnum.MMC_MBC2;
				BatterySupport = 1;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_RAM_BATT:
				MMCType = MMCTypesEnum.MMC_ROMONLY;
				BatterySupport = 1;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;

			case CART_ROM_MMM01:
			case CART_ROM_MMM01_SRAM:
				MMCType = MMCTypesEnum.MMC_MMM01;
				BatterySupport = 0;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_MMM01_SRAM_BATT:
				MMCType = MMCTypesEnum.MMC_MMM01;
				BatterySupport = 1;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;

			case CART_ROM_MBC3:
			case CART_ROM_MBC3_RAM:
				MMCType = MMCTypesEnum.MMC_MBC3;
				BatterySupport = 0;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_MBC3_RAM_BATT:
				MMCType = MMCTypesEnum.MMC_MBC3;
				BatterySupport = 1;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_MBC3_TIMER_BATT:
			case CART_ROM_MBC3_TIMER_RAM_BATT:
				MMCType = MMCTypesEnum.MMC_MBC3;
				BatterySupport = 1;
				RumbleSupport = 0;
				RTCSupport = 1;
				break;

			case CART_ROM_MBC5:
			case CART_ROM_MBC5_RAM:
				MMCType = MMCTypesEnum.MMC_MBC5;
				BatterySupport = 0;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_MBC5_RAM_BATT:
				MMCType = MMCTypesEnum.MMC_MBC5;
				BatterySupport = 1;
				RumbleSupport = 0;
				RTCSupport = 0;
				break;
			case CART_ROM_MBC5_RUMBLE:
			case CART_ROM_MBC5_RUMBLE_SRAM:
				MMCType = MMCTypesEnum.MMC_MBC5;
				BatterySupport = 0;
				RumbleSupport = 1;
				RTCSupport = 0;
				break;
			case CART_ROM_MBC5_RUMBLE_SRAM_BATT:
				MMCType = MMCTypesEnum.MMC_MBC5;
				BatterySupport = 1;
				RumbleSupport = 1;
				RTCSupport = 0;
				break;

			case CART_POCKET_CAMERA:
			case CART_BANDAI_TAMA5:
				break;

			default:
				MMCType = MMCTypesEnum.MMC_UNKNOWN;
				BatterySupport = 0;
				RumbleSupport = 0;
				RTCSupport = 0;
		}

		if (MMCType == MMCTypesEnum.MMC_MBC2)
		{
			RAMSize = 512;
		}
	}
}
