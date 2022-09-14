package edu.school21.GMBU.emulator.MMC;

public class MMC_MBC1 extends MMC_MBC implements MMC {


	private static final int MBC1MODE_16_8 = 0;
	private static final int MBC1MODE_4_32 = 1;
	private static final int ROMBankSize = 0x4000;
	private static final int RAMBankSize = 0x2000;

	int Mode;

	public MMC_MBC1(byte[] ROM, int ROMSize, byte[] RAMBanks, int RAMSize) {
		super(ROM, ROMSize, RAMBanks, RAMSize);
		this.RAMEnabled = false;
		this.Mode = MBC1MODE_16_8;
		this.RAMOffset = 0;
		this.ROMOffset = ROMBankSize;
	}


	@Override
	public byte read(int address) {
		{
			switch (address & 0xF000)
			{
				//ROM bank 0
				case 0x0000:
				case 0x1000:
				case 0x2000:
				case 0x3000:
					return ROM[address];

				//ROM bank 1
				case 0x4000:
				case 0x5000:
				case 0x6000:
				case 0x7000:
					return ROM[ROMOffset + (address - 0x4000)];

				//Switchable RAM bank
				case 0xA000:
				case 0xB000:
					if (RAMEnabled)
					{
						if (Mode == MBC1MODE_16_8)
						{
							return RAMBanks[address - 0xA000];
						}
						else
						{
							return RAMBanks[RAMOffset + (address - 0xA000)];
						}
					}
			}
			return (byte)0xFF;
		}
	}

	@Override
	public void write(int address, byte value) {
		switch (address & 0xF000)
		{
			//RAM enable/disable
			case 0x0000:
			case 0x1000:
				RAMEnabled = (value & 0x0F) == 0x0A;
				break;

			//ROM bank switching (5 LSB)
			case 0x2000:
			case 0x3000:
				//Setting 5 LSB (0x1F) of ROMOffset without touching 2 MSB (0x60)
				ROMOffset = (((ROMOffset / ROMBankSize) & 0x60) | (value & 0x1F));
				ROMOffset %= ROMSize;

				if (ROMOffset == 0x00) ROMOffset = 0x01;
				else if (ROMOffset == 0x20) ROMOffset = 0x21;
				else if (ROMOffset == 0x40) ROMOffset = 0x41;
				else if (ROMOffset == 0x60) ROMOffset = 0x61;

				ROMOffset *= ROMBankSize;
				break;

			//RAM bank switching/Writing 2 MSB of ROM bank address
			case 0x4000:
			case 0x5000:
				if (Mode == MBC1MODE_16_8)
				{
					ROMOffset = ((ROMOffset / ROMBankSize) & 0x1F) | ((value & 0x3) << 5);
					ROMOffset %= ROMSize;

					if (ROMOffset == 0x00) ROMOffset = 0x01;
					else if (ROMOffset == 0x20) ROMOffset = 0x21;
					else if (ROMOffset == 0x40) ROMOffset = 0x41;
					else if (ROMOffset == 0x60) ROMOffset = 0x61;

					ROMOffset *= ROMBankSize;
				}
				else
				{
					RAMOffset = value & 0x3;
					RAMOffset %= RAMSize;
					RAMOffset *= RAMBankSize;
				}
				break;

			//MBC1 mode switching
			case 0x6000:
			case 0x7000:
				if ((value & 0x1) == 0)
				{
					Mode = MBC1MODE_16_8;
				}
				else
				{
					Mode = MBC1MODE_4_32;
				}
				break;

			//Switchable RAM bank
			case 0xA000:
			case 0xB000:
				if (RAMEnabled)
				{
					if (Mode == MBC1MODE_16_8)
					{
						RAMBanks[address - 0xA000] = value;
					}
					else
					{
						RAMBanks[RAMOffset + (address - 0xA000)] = value;
					}
				}
				break;
		}
	}
}
