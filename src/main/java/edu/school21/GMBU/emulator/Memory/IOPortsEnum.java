package edu.school21.GMBU.emulator.Memory;

import java.util.HashMap;
import java.util.Map;

public enum IOPortsEnum {
	IOPORT_P1(0x00),			//Register for reading joy pad info (R/W)
	IOPORT_SB(0x01),			//Serial transfer data (R/W)
	IOPORT_SC(0x02),			//Serial IO control (R/W)
	IOPORT_DIV(0x04),			//Divider Register. Writing any value sets it to $00 (R/W)
	IOPORT_TIMA(0x05),			//Timer counter (R/W)
	IOPORT_TMA(0x06),			//Timer Modulo (R/W)
	IOPORT_TAC(0x07),			//Timer Control (R/W)
	IOPORT_IF(0x0F),			//Interrupt Flag (R/W)
	IOPORT_NR10(0x10),			//Sound Mode 1 register)sweep register (R/W)
	IOPORT_NR11(0x11),			//Sound Mode 1 register)Sound length/Wave pattern duty (R/W)
	IOPORT_NR12(0x12),			//Sound Mode 1 register)Envelope (R/W)
	IOPORT_NR13(0x13),			//Sound Mode 1 register)Frequency lo (W)
	IOPORT_NR14(0x14),			//Sound Mode 1 register)Frequency hi (R/W)
	IOPORT_NR20(0x15),			//Not used
	IOPORT_NR21(0x16),			//Sound Mode 2 register)Sound Length; Wave Pattern Duty (R/W)
	IOPORT_NR22(0x17),			//Sound Mode 2 register)envelope (R/W)
	IOPORT_NR23(0x18),			//Sound Mode 2 register)frequency lo data (W)
	IOPORT_NR24(0x19),			//Sound Mode 2 register)frequency hi data (R/W)
	IOPORT_NR30(0x1A),			//Sound Mode 3 register)Sound on/off (R/W)
	IOPORT_NR31(0x1B),			//Sound Mode 3 register)sound length (R/W)
	IOPORT_NR32(0x1C),			//Sound Mode 3 register)Select output level (R/W)
	IOPORT_NR33(0x1D),			//Sound Mode 3 register)frequency's lower data (W)
	IOPORT_NR34(0x1E),			//Sound Mode 3 register)frequency's higher data (R/W)
	IOPORT_NR40(0x1F),			//Not used
	IOPORT_NR41(0x20),			//Sound Mode 4 register)sound length (R/W)
	IOPORT_NR42(0x21),			//Sound Mode 4 register)envelope (R/W)
	IOPORT_NR43(0x22),			//Sound Mode 4 register)polynomial counter (R/W)
	IOPORT_NR44(0x23),			//Sound Mode 4 register)counter/consecutive; inital (R/W)
	IOPORT_NR50(0x24),			//Channel control / ON-OFF / Volume (R/W)
	IOPORT_NR51(0x25),			//Selection of Sound output terminal (R/W)
	IOPORT_NR52(0x26),			//Sound on/off (R/W)
	IOPORT_WAVEPATRAM_0(0x30),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_1(0x31),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_2(0x32),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_3(0x33),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_4(0x34),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_5(0x35),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_6(0x36),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_7(0x37),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_8(0x38),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_9(0x39),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_A(0x3A),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_B(0x3B),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_C(0x3C),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_D(0x3D),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_E(0x3E),	//Waveform storage for arbitrary sound data
	IOPORT_WAVEPATRAM_F(0x3F),	//Waveform storage for arbitrary sound data
	IOPORT_LCDC(0x40),			//LCD Control (R/W)
	IOPORT_STAT(0x41),			//LCDC Status (R/W)
	IOPORT_SCY(0x42),			//Scroll Y (R/W)
	IOPORT_SCX(0x43),			//Scroll X (R/W)
	IOPORT_LY(0x44),			//LCDC Y-Coordinate. Writing will reset the counter (R)
	IOPORT_LYC(0x45),			//LY Compare (R/W)
	IOPORT_DMA(0x46),			//DMA Transfer and Start Address (W)
	IOPORT_BGP(0x47),			//BG & Window Palette Data (R/W)
	IOPORT_OBP0(0x48),			//Object Palette 0 Data (R/W)
	IOPORT_OBP1(0x49),			//Object Palette 1 Data (R/W)
	IOPORT_WY(0x4A),			//Window Y Position (R/W)
	IOPORT_WX(0x4B),			//Window X Position (R/W)
	IOPORT_KEY1(0x4D),			//Prepare Speed Switch
	IOPORT_VBK(0x4F),			//Current Video Memory (VRAM) Bank
	IOPORT_INBIOS(0x50),
	IOPORT_HDMA1(0x51),			//New DMA Source)High
	IOPORT_HDMA2(0x52),			//New DMA Source)Low
	IOPORT_HDMA3(0x53),			//New DMA Destination)High
	IOPORT_HDMA4(0x54),			//New DMA Destination)Low
	IOPORT_HDMA5(0x55),			//New DMA Length/Mode/Start
	IOPORT_BGPI(0x68),			//Background Palette Index
	IOPORT_BGPD(0x69),			//Background Palette Data
	IOPORT_OBPI(0x6A),			//Sprite Palette Index
	IOPORT_OBPD(0x6B),			//Sprite Palette Data
	IOPORT_HID1(0x6C),
	IOPORT_SVBK(0x70),			//WRAM Bank
	IOPORT_HID2(0x72),
	IOPORT_HID3(0x73),
	IOPORT_HID4(0x74),
	IOPORT_HID5(0x75),

	IO_PORTS_76(0x76),
	IO_PORTS_77(0x77),
	IOPORT_INT(0xFF);

	private int ioport;

	private static Map<Integer, IOPortsEnum> map = new HashMap<Integer, IOPortsEnum>();

	static {
		for (int i = 0; i < 0xFF; i++) {
			map.put(i, IOPORT_INT);
		}
		for (IOPortsEnum ioports : IOPortsEnum.values()) {
			map.put(ioports.ioport, ioports);
		}
	}

	private IOPortsEnum(final int ioport) { this.ioport = ioport; }

	public static IOPortsEnum valueOf(int ioport) {
		return map.get(ioport);
	}
}
