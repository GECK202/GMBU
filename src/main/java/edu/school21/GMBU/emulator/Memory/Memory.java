package edu.school21.GMBU.emulator.Memory;

import edu.school21.GMBU.emulator.MMC.MMC;
import edu.school21.GMBU.emulator.MMC.MMC_MBC1;
import edu.school21.GMBU.emulator.RomInfo.RomInfo;
import edu.school21.GMBU.emulator.RomInfo.RomLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

@Component
public class Memory {
    private static final int WRAMBankSize = 0x1000;

    private static final int ROMBankSize = 0x4000;
    private static final int RAMBankSize = 0x2000;


    private byte[] memory;

    @Autowired
    private RomInfo romInfo;

    @Autowired
    private RomLoader romLoader;


    private Memory() {
        memory = new byte[0xFFFF];
    }

    boolean CGB;
    boolean CGBDoubleSpeed;

    boolean InBIOS;
    static byte[] BIOS  = new byte[0x100];

                                                        //Memory map
                                                        //RAM section				Start    End	Comments
                                                        // -------------------------------------------------------------------------------------------------------------------
    byte[] ROM;								            //ROM banks 0, 1			0x0000 - 0x7FFF - holds entire ROM including ROM banks.
                                                        //byte VRAM[0x2000];
                                                        //Video RAM 				0x8000 - 0x9FFF - Video RAM. Holds tile patterns
    byte[] RAMBanks;							        //Switchable RAM bank		0xA000 - 0xBFFF - holds cartridge RAM banks
    byte[] WRAM = new byte [WRAMBankSize * 8];			//Work RAM banks			0xC000 - 0xDFFF
                                                        //Work RAM echo				0xE000 - 0xFDFF
                                                        //byte OAM[0xA0];
                                                        //Sprite attrib memory		0xFE00 - 0xFE9F - Holds sprites information (position, pattern, flags, color)
                                                        //Unusable RAM				0xFEA0 - 0xFEFF
                                                        //byte IOPorts[0x80];
                                                        //I/0 ports					0xFF00 - 0xFF7F - holds joypad, GPU, sound, serial port, timers, interrupt registers
    byte[] HRAM = new byte [0x7F];			            //Hight RAM					0xFF80 - 0xFFFE - Additional Gameboy RAM. Usually holds stack
                                                        //byte IE;
                                                        //Interrupt enable reg		0xFFFF
                                                        //
                                                        // Commented parts of memory mentioned just for clarification and actually located in appropriate classes

    int WRAMOffset;                                     //Points to current WRAM bank address
    private MMC MBC;

    //Cartridge
    boolean ROMLoaded;
    RomLoader ROMLoader;

    //Undocumented registers
    byte HIDDEN1;
    byte HIDDEN2;
    byte HIDDEN3;
    byte HIDDEN4;
    byte HIDDEN5;



    /*
    public boolean write(int address, byte value){
        if (address >= 0 && address <0xFFFF) {
            memory[address] = value;
            return true;
        }
        return  false;
    }

    public byte read(int address) {
        if (address >= 0 && address <0xFFFF) {
            return memory[address];
        }
        return 0;
    }
*/
    //****************************************
    //****************************************
    //****************************************

    private boolean checkRom() {
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

    public boolean loadRom(String fileName) {
        try (FileInputStream fin = new FileInputStream(fileName)) {
            ROM = new byte[fin.available()];
            int count = fin.read(ROM, 0, fin.available());
            if (count > 0) {
                if (checkRom()) {
                    //ROMLoader = RomLoader.getShared();
                    romLoader.setRomInfo(ROM);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadCartridge(String fileName) {

        boolean res = loadRom(fileName);
        if (res) {
            this.setting(ROM.length);
        }
        return res;
    }

    private void setting(int filesize) {
        //LoadedROMInfo.ReadROMInfo(ROM);
        //strncpy_s(LoadedROMInfo.ROMFile, ROMPath, 250);
        romLoader.ROMFile[250] = '\0';

        if (romLoader.ROMSize == 0)
        {
            romLoader.ROMSize = filesize / ROMBankSize;
        }

        int RAMSize = (romLoader.RAMSize == 0) ? 1 : romLoader.RAMSize;
        RAMBanks = new byte[RAMSize * RAMBankSize];

        //On power ON cartridge RAM is filled with random values
        //srand(clock());
        for (int i = 0; i < RAMSize * RAMBankSize; i++)
        {
            RAMBanks[i] = (byte) (Math.random() % 0x100);
        }

        switch(romLoader.MMCType)
        {
            case MMC_ROMONLY:
                //MBC = new MBC_ROMOnly(ROM, LoadedROMInfo.ROMSize, RAMBanks, RAMSize);
                break;

            case MMC_MBC1:
                MBC = new MMC_MBC1(ROM, romLoader.ROMSize, RAMBanks, RAMSize);
                break;

            case MMC_MBC2:
                //MBC = new MBC2(ROM, LoadedROMInfo.ROMSize, RAMBanks, RAMSize);
                break;

            case MMC_MBC3:
                //MBC = new MBC3(ROM, LoadedROMInfo.ROMSize, RAMBanks, RAMSize);
                break;

            case MMC_MBC5:
                //MBC = new MBC5(ROM, LoadedROMInfo.ROMSize, RAMBanks, RAMSize);
                break;

            case MMC_MMM01:
                //MBC = new MBC_MMM01(ROM, LoadedROMInfo.ROMSize, RAMBanks, RAMSize);
                break;

            default:
                //delete[] ROM;
                ROM = new byte [0];
                ROMLoaded = false;
        }

        /*
        switch (mode)
        {
            case EMULMODE_FORCEDMG:
                CGB = false;
                CGBDoubleSpeed = false;
                break;

            case EMULMODE_FORCECGB:
                CGB = true;
                CGBDoubleSpeed = false;
                break;

            case EMULMODE_AUTO:
                switch (ROM[0x143])
                {
                    case 0x80://Support CGB and DMG
                        CGB = true;
                        CGBDoubleSpeed = false;
                        break;

                    case 0xC0://CGB only
                        CGB = true;
                        CGBDoubleSpeed = false;
                        break;

                    default://DMG only
                        CGB = false;
                        CGBDoubleSpeed = false;
                        break;
                }
                break;

            default:
                break;
        }
         */

        ROMLoaded = true;

        //Restoring RAM contents if there is battery
        //LoadRAM();

        //return &LoadedROMInfo;
    }

    public boolean write(short address, byte value)
    {
        switch (address & 0xF000)
        {
            //ROM bank 0
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
                //Switchable ROM bank
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                //Switchable RAM bank
            case 0xA000:
            case 0xB000:
                if (!InBIOS || address >= 0x100)
                {
                    MBC.write(address, value);
                }
                break;

            //Video RAM
            case 0x8000:
            case 0x9000:
                //GPU.WriteVRAM(address- 0x8000, value);
                break;

            //Work RAM bank 0
            case 0xC000:
                WRAM[(short)(address - 0xC000)] = value;
                break;

            //Switchable work RAM bank
            case 0xD000:
                WRAM[(short)(WRAMOffset + (short)(address- 0xD000))] = value;
                break;

            //Work RAM bank 0 echo
            case 0xE000:
                WRAM[address- 0xE000] = value;
                break;

            case 0xF000:
                switch (address& 0xF00)
                {
                    //Switchable Work RAM bank echo
                    case 0x000:
                    case 0x100:
                    case 0x200:
                    case 0x300:
                    case 0x400:
                    case 0x500:
                    case 0x600:
                    case 0x700:
                    case 0x800:
                    case 0x900:
                    case 0xA00:
                    case 0xB00:
                    case 0xC00:
                    case 0xD00:
                        WRAM[WRAMOffset + (address - 0xF000)] = value;
                        break;


                    //Sprite attrib memory
                    case 0xE00:
                        //GPU.WriteOAM(address - 0xFE00, value);
                        break;

                    case 0xF00:

                        switch (IOPortsEnum.valueOf(address & 0xFF))
                        {
                            //*
                            case IOPORT_P1:
                                //Joypad.P1Changed(value);
                                break;

                            case IOPORT_SB:
                                //Serial.SBChanged(value);
                                break;

                            case IOPORT_SC:
                                //Serial.SCChanged(value);
                                break;

                            case IOPORT_DIV:
                                //DIV.DIVChanged(value);
                                break;

                            case IOPORT_TIMA:
                                //TIMA.TIMAChanged(value);
                                break;

                            case IOPORT_TMA:
                                //TIMA.TMAChanged(value);
                                break;

                            case IOPORT_TAC:
                                //TIMA.TACChanged(value);
                                break;

                            case IOPORT_IF:
                                //INT.IFChanged(value);
                                break;

                            //Sound mode 1 registers
                            case IOPORT_NR10:
                                //Sound.NR10Changed(value);
                                break;

                            case IOPORT_NR11:
                                //Sound.NR11Changed(value);
                                break;

                            case IOPORT_NR12:
                                //Sound.NR12Changed(value);
                                break;

                            case IOPORT_NR13:
                                //Sound.NR13Changed(value);
                                break;

                            case IOPORT_NR14:
                                //Sound.NR14Changed(value);
                                break;

                            //Sound mode 2 registers
                            case IOPORT_NR21:
                                //Sound.NR21Changed(value);
                                break;

                            case IOPORT_NR22:
                                //Sound.NR22Changed(value);
                                break;

                            case IOPORT_NR23:
                                //Sound.NR23Changed(value);
                                break;

                            case IOPORT_NR24:
                                //Sound.NR24Changed(value);
                                break;

                            //Sound mode 3 registers
                            case IOPORT_NR30:
                                //Sound.NR30Changed(value);
                                break;

                            case IOPORT_NR31:
                                //Sound.NR31Changed(value);
                                break;

                            case IOPORT_NR32:
                                //Sound.NR32Changed(value);
                                break;

                            case IOPORT_NR33:
                                //Sound.NR33Changed(value);
                                break;

                            case IOPORT_NR34:
                                //Sound.NR34Changed(value);
                                break;

                            //Sound mode 4 registers
                            case IOPORT_NR41:
                                //Sound.NR41Changed(value);
                                break;

                            case IOPORT_NR42:
                                //Sound.NR42Changed(value);
                                break;

                            case IOPORT_NR43:
                                //Sound.NR43Changed(value);
                                break;

                            case IOPORT_NR44:
                                //Sound.NR44Changed(value);
                                break;

                            //Misc registers
                            case IOPORT_NR50:
                                //Sound.NR50Changed(value);
                                break;

                            case IOPORT_NR51:
                                //Sound.NR51Changed(value);
                                break;

                            case IOPORT_NR52:
                                //Sound.NR52Changed(value);
                                break;

                            case IOPORT_WAVEPATRAM_0:
                            case IOPORT_WAVEPATRAM_1:
                            case IOPORT_WAVEPATRAM_2:
                            case IOPORT_WAVEPATRAM_3:
                            case IOPORT_WAVEPATRAM_4:
                            case IOPORT_WAVEPATRAM_5:
                            case IOPORT_WAVEPATRAM_6:
                            case IOPORT_WAVEPATRAM_7:
                            case IOPORT_WAVEPATRAM_8:
                            case IOPORT_WAVEPATRAM_9:
                            case IOPORT_WAVEPATRAM_A:
                            case IOPORT_WAVEPATRAM_B:
                            case IOPORT_WAVEPATRAM_C:
                            case IOPORT_WAVEPATRAM_D:
                            case IOPORT_WAVEPATRAM_E:
                            case IOPORT_WAVEPATRAM_F:
                                //Sound.WaveRAMChanged((address& 0xFF) - IOPORT_WAVEPATRAM, value);
                                break;

                            case IOPORT_LCDC:
                                //GPU.LCDCChanged(value);
                                break;

                            case IOPORT_STAT:
                                //GPU.STATChanged(value);
                                break;

                            case IOPORT_SCY:
                                //GPU.SCYChanged(value);
                                break;

                            case IOPORT_SCX:
                                //GPU.SCXChanged(value);
                                break;

                            case IOPORT_LY:
                                //GPU.LYChanged(value);
                                break;

                            case IOPORT_LYC:
                                //GPU.LYCChanged(value);
                                break;

                            case IOPORT_DMA:
                                //GPU.DMAChanged(value, *this);
                                break;

                            case IOPORT_BGP:
                                //GPU.BGPChanged(value);
                                break;

                            case IOPORT_OBP0:
                                //GPU.OBP0Changed(value);
                                break;

                            case IOPORT_OBP1:
                                //GPU.OBP1Changed(value);
                                break;

                            case IOPORT_WY:
                                //GPU.WYChanged(value);
                                break;

                            case IOPORT_WX:
                                //GPU.WXChanged(value);
                                break;

                            case IOPORT_KEY1:
                                //CGBSpeedSwitcher.KEY1Changed(value);
                                break;

                            case IOPORT_VBK:
                                //GPU.VBKChanged(value);
                                break;

                            case IOPORT_HDMA1:
                                //GPU.HDMA1Changed(value);
                                break;

                            case IOPORT_HDMA2:
//                                GPU.HDMA2Changed(value);
                                break;

                            case IOPORT_HDMA3:
//                                GPU.HDMA3Changed(value);
                                break;

                            case IOPORT_HDMA4:
//                                GPU.HDMA4Changed(value);
                                break;

                            case IOPORT_HDMA5:
//                                GPU.HDMA5Changed(value, *this);
                                break;

                            case IOPORT_BGPI:
//                                GPU.BGPIChanged(value);
                                break;

                            case IOPORT_BGPD:
//                                GPU.BGPDChanged(value);
                                break;

                            case IOPORT_OBPI:
//                                GPU.OBPIChanged(value);
                                break;

                            case IOPORT_OBPD:
//                                GPU.OBPDChanged(value);
                                break;

                            case IOPORT_INBIOS:
                                //Writing to 0xFF50 disables BIOS
                                //This is done by last instruction in bootstrap ROM
                                InBIOS = false;
                                break;

                            case IOPORT_SVBK:
                                WRAMOffset = (value & 0x7) * WRAMBankSize;
                                if (WRAMOffset == 0)
                                {
                                    WRAMOffset = WRAMBankSize;
                                }
                                break;

                            case IOPORT_INT:
//                                INT.IEChanged(value);
                                break;

                            //Undocumented registers
                            case IOPORT_HID1:
                                if (CGB)
                                {
                                    HIDDEN1 = value;
                                }
                                break;

                            case IOPORT_HID2:
                                HIDDEN2 = value;
                                break;

                            case IOPORT_HID3:
                                HIDDEN3 = value;
                                break;

                            case IOPORT_HID4:
                                HIDDEN4 = value;
                                break;

                            case IOPORT_HID5:
                                HIDDEN5 = value;
                                break;

                            default:
                                if (address>= 0xFF80 && address<= 0xFFFE)//Internal RAM
                                {
                                    HRAM[address- 0xFF80] = value;
                                }
                                break;
                        }
                }
                break;
        }
        return true;
    }

    public byte read(short address)
    {

        switch (address & 0xF000)
        {
            //ROM bank 0
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
                //Switchable ROM bank
            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                //Switchable RAM bank
            case 0xA000:
            case 0xB000:
                if (InBIOS && address< 0x100)
                {
                    return BIOS[address];
                }
                else
                {
                    return MBC.read(address);
                }


                //Video RAM
            case 0x8000:
            case 0x9000:
                return 0;//GPU.ReadVRAM(address- 0x8000);

            //Work RAM bank 0
            case 0xC000:
                return WRAM[(short)(address- 0xC000)];

            //Switchable Work RAM bank
            case 0xD000:
                return WRAM[(short)(WRAMOffset + (short)(address - 0xD000))];

            //Work RAM bank 0 echo
            case 0xE000:
                short ad = (short)(address - 0xE000);
                return WRAM[ad];

            case 0xF000:
                switch (address& 0xF00)
                {
                    //Switchable Work RAM bank echo
                    case 0x000:
                    case 0x100:
                    case 0x200:
                    case 0x300:
                    case 0x400:
                    case 0x500:
                    case 0x600:
                    case 0x700:
                    case 0x800:
                    case 0x900:
                    case 0xA00:
                    case 0xB00:
                    case 0xC00:
                    case 0xD00:
                        return WRAM[(short)(WRAMOffset + (short)(address- 0xF000))];


                    //Sprite attrib memory
                    case 0xE00:
                        return 0; //GPU.ReadOAM(address- 0xFE00);

                    case 0xF00:

                        switch (IOPortsEnum.valueOf(address & 0xFF))
                        {
                            case IOPORT_P1:
//                                return Joypad.GetP1();

                            case IOPORT_SB:
//                                return Serial.GetSB();

                            case IOPORT_SC:
///                                return Serial.GetSC();

                            case IOPORT_DIV:
//                                return DIV.GetDIV();

                            case IOPORT_TIMA:
//                                return TIMA.GetTIMA();

                            case IOPORT_TMA:
//                                return TIMA.GetTMA();

                            case IOPORT_TAC:
//                                return TIMA.GetTAC();

                            case IOPORT_IF:
//                                return INT.GetIF();

                            case IOPORT_NR10:
//                                return Sound.GetNR10();

                            case IOPORT_NR11:
//                                return Sound.GetNR11();

                            case IOPORT_NR12:
//                                return Sound.GetNR12();

                            case IOPORT_NR13:
//                                return Sound.GetNR13();

                            case IOPORT_NR14:
//                                return Sound.GetNR14();

                            case IOPORT_NR21:
//                                return Sound.GetNR21();

                            case IOPORT_NR22:
//                                return Sound.GetNR22();

                            case IOPORT_NR23:
//                                return Sound.GetNR23();

                            case IOPORT_NR24:
//                                return Sound.GetNR24();

                            case IOPORT_NR30:
//                                return Sound.GetNR30();

                            case IOPORT_NR31:
//                                return Sound.GetNR31();

                            case IOPORT_NR32:
//                                return Sound.GetNR32();

                            case IOPORT_NR33:
//                                return Sound.GetNR33();

                            case IOPORT_NR34:
//                                return Sound.GetNR34();

                            case IOPORT_NR41:
//                                return Sound.GetNR41();

                            case IOPORT_NR42:
//                                return Sound.GetNR42();

                            case IOPORT_NR43:
//                                return Sound.GetNR43();

                            case IOPORT_NR44:
//                                return Sound.GetNR44();

                            case IOPORT_NR50:
//                                return Sound.GetNR50();

                            case IOPORT_NR51:
//                                return Sound.GetNR51();

                            case IOPORT_NR52:
//                                return Sound.GetNR52();

                            case IOPORT_WAVEPATRAM_0:
                            case IOPORT_WAVEPATRAM_1:
                            case IOPORT_WAVEPATRAM_2:
                            case IOPORT_WAVEPATRAM_3:
                            case IOPORT_WAVEPATRAM_4:
                            case IOPORT_WAVEPATRAM_5:
                            case IOPORT_WAVEPATRAM_6:
                            case IOPORT_WAVEPATRAM_7:
                            case IOPORT_WAVEPATRAM_8:
                            case IOPORT_WAVEPATRAM_9:
                            case IOPORT_WAVEPATRAM_A:
                            case IOPORT_WAVEPATRAM_B:
                            case IOPORT_WAVEPATRAM_C:
                            case IOPORT_WAVEPATRAM_D:
                            case IOPORT_WAVEPATRAM_E:
                            case IOPORT_WAVEPATRAM_F:
//                                return Sound.GetWaveRAM((address& 0xFF) - IOPORT_WAVEPATRAM);

                            case IOPORT_LCDC:
//                                return GPU.GetLCDC();

                            case IOPORT_STAT:
//                                return GPU.GetSTAT();

                            case IOPORT_SCY:
//                                return GPU.GetSCY();

                            case IOPORT_SCX:
//                                return GPU.GetSCX();

                            case IOPORT_LY:
//                                return GPU.GetLY();

                            case IOPORT_LYC:
//                                return GPU.GetLYC();

                            case IOPORT_BGP:
//                                return GPU.GetBGP();

                            case IOPORT_OBP0:
//                                return GPU.GetOBP0();

                            case IOPORT_OBP1:
//                                return GPU.GetOBP1();

                            case IOPORT_WY:
//                                return GPU.GetWY();

                            case IOPORT_WX:
//                                return GPU.GetWX();

                            case IOPORT_KEY1:
//                                return CGBSpeedSwitcher.GetKEY1();

                            case IOPORT_VBK:
//                                return GPU.GetVBK();

                            case IOPORT_HDMA1:
//                                return GPU.GetHDMA1();

                            case IOPORT_HDMA2:
//                                return GPU.GetHDMA2();

                            case IOPORT_HDMA3:
//                                return GPU.GetHDMA3();

                            case IOPORT_HDMA4:
//                                return GPU.GetHDMA4();

                            case IOPORT_HDMA5:
//                                return GPU.GetHDMA5();

                            case IOPORT_BGPI:
//                                return GPU.GetBGPI();

                            case IOPORT_BGPD:
//                                return GPU.GetBGPD();

                            case IOPORT_OBPI:
//                                return GPU.GetOBPI();

                            case IOPORT_OBPD:
//                                return GPU.GetOBPD();
                                    return 0;


                            case IOPORT_SVBK:
                                return (byte)(0xF8 | ((WRAMOffset / WRAMBankSize) & 0x7));

                            case IOPORT_INT:
                                return 0;//INT.GetIE();

                            //Undocumented registers
                            case IOPORT_HID1:
                                if (CGB)
                                {
                                    return (byte)(0xFE | HIDDEN1);
                                }
                                else
                                {
                                    return (byte)0xFF;
                                }

                            case IOPORT_HID2:
                                return HIDDEN2;

                            case IOPORT_HID3:
                                return HIDDEN3;

                            case IOPORT_HID4:
                                if (CGB)
                                {
                                    return HIDDEN4;
                                }
                                else
                                {
                                    return (byte)0xFF;
                                }

                            case IOPORT_HID5:
                                return (byte)(0x70 | HIDDEN5);

                            case IO_PORTS_76:
                            case IO_PORTS_77:
                                return 0x00;

                            default:
                                if (address >= 0xFF80 && address <= 0xFFFE)//Internal RAM
                                {
                                    return HRAM[address- 0xFF80];
                                }
                                else
                                {
                                    return (byte)0xFF;
                                }
                        }

                    default:
                        return (byte)0xFF;
                }

            default:
                return (byte)0xFF;
        }
    }
    
    //****************************************
    //****************************************
    //****************************************
    
    
    
    
    
    
    
    public short readWord(int address) {
        short value = read((short) address);
        value |= read((short) (address + 1)) << 8;
        return value;
    }

    public void writeWord(int address, short value) {
        write((short) address, (byte)value);
        write((short) (address + 1), (byte)(value>>8));
    }
}
