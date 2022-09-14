package edu.school21.GMBU.emulator;

import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CPU {


	private int PC = 4;
	private int SP;
	private Register AF;
	private Register BC;
	private Register DE;
	private Register HL;

	private String curName = "error";

	private Opcode curCode;

	private Opcode[] opname;

	@Autowired
	private Memory memory;

	public boolean getBit(byte bt, int position) {
		if (position >= 0 && position < 8) { return ((bt >> position) & 1) != 0; }
		return false;
	}

	private void setHFlag(boolean flag) {
		AF.setBit(flag, 5);
	}

	private void setHFlag(byte op1, byte op2, byte result) {
		boolean resBit = getBit(result, 4);
		if (getBit(op1, 4) == getBit(op2, 4)) {
			AF.setBit(resBit, 5);
		} else {
			AF.setBit(!resBit, 5);
		}
	}

	private void setHFlag(short op1, short op2, short result) {
		setHFlag((byte)(op1 >> 8), (byte)(op2 >> 8), (byte)(result >> 8));
	}

	private void setZFlag(boolean flag) {
		AF.setBit(flag, 7);
	}

	private void setZFlag(byte result) {
		AF.setBit((result == (byte)0), 7);
	}

	private void setNFlag(boolean flag) {
		AF.setBit(flag, 6);
	}

	private void setCFlag(boolean flag) {
		AF.setBit(flag, 4);
	}

	private void LDHL(Register r, byte srcL, byte srcH) {
		r.setHigh(srcH);
		r.setLow(srcL);
	}

	private void LD(Register r, RegisterByte rb, byte value) {
		r.setByte(rb, value);
	}

	private void LDM(short address, byte value) {
		memory.write(address, value);
	}

	private void INCR(Register r) {
		r.set((short) (r.get() + 1));
	}

	private void INCRB(Register r, RegisterByte rb) {
		byte reg = r.getByte(rb);
		INCR(r);
		byte res = r.getByte(rb);
		setHFlag(reg, (byte)0, res);
		setZFlag(res);
		setNFlag(false);
	}

	private void DECR(Register r) {
		r.set((short) (r.get() - 1));
	}

	private void DECRB(Register r, RegisterByte rb) {
		byte reg = r.getByte(rb);
		DECR(r);
		byte res = r.getByte(rb);
		setHFlag(reg, (byte)0, res);
		setZFlag(res);
		setNFlag(true);
	}

	{

		opname = new Opcode[256];
		opname[0x00] = new Opcode(1, 4, 0, (index)-> {
			curName = "0x00: NOP";
		});
		opname[0x01] = new Opcode(3, 12, 1, (index)-> {

			//Memory m = Memory.getShared();
			curName = "0x01: LD BC,d16";
            byte srcL = memory.read((short) PC);
            PC++;
            byte srcH = memory.read((short) PC);
            PC++;
            LDHL(BC, srcL, srcH);
		});

		opname[0x02] = new Opcode(1, 8, 2, (index)-> {

			short address = memory.read(BC.get());
			curName = "0x02: LD (BC), A";
			LDM(address, AF.getHigh());
		});

		opname[0x03] = new Opcode(1,8,3, (index)-> {
			curName = "0x03: INC BC";
			INCR(BC);
		});

		opname[0x04] = new Opcode(1,4,4, (index)-> {
			curName = "0x04: INC B";
			INCRB(BC, RegisterByte.HIGH);
		});

		opname[0x05] = new Opcode(1,4,5, (index)-> {
			curName = "0x05: DEC B";
			DECRB(BC, RegisterByte.HIGH);
		});

		opname[0x06] = new Opcode(2, 8, 6, (index)-> {
			//Memory m = Memory.getShared();
			curName = "0x06: LD B d8";

			byte value = memory.read((short) PC);
			PC++;
			LD(BC, RegisterByte.HIGH, value);
		});


		opname[0x07] = new Opcode(1,4,7, (index)-> {

			curName = "0x07: RLCA";
			byte a = AF.getHigh();
			boolean b7 = getBit(a, 7);
			setZFlag(false);
			setHFlag(false);
			setNFlag(false);
			setCFlag(b7);
			AF.setHigh((byte)(a<<1));
			AF.setBitByte(RegisterByte.HIGH, b7, 0);
		});

		opname[0x08] = new Opcode(3,20,8, (index)-> {
			//Memory m = Memory.getShared();
			curName = "0x08: LD (a16) SP";
			short value = memory.read((short)PC);
			PC++;
			value |= ((short)memory.read((short)PC))<<8;
			PC++;
			memory.writeWord(SP, value);
		});

		opname[0x09] = new Opcode(1, 8, 9, (index)-> {
			curName = "0x09: ADD HL BC";
			int op1 = HL.get();
			int op2 = BC.get();
			int result = op1 + op2;
			boolean cFlag = (result >> 16) != 0;
			setHFlag((short) op1, (short) op2, (short) result);
			setNFlag(false);
			setCFlag(cFlag);
		});

		for (int i = 10; i < 256; i++) {
			opname[i] = new Opcode(1, 4, i, (index)-> {
				curName = String.format("0x%02x: COMMAND", index);
			});
		}
	}

	private CPU() {
		PC = 0;
		AF = new Register((short) 0);
		BC = new Register((short) 0);
		DE = new Register((short) 0);
		HL = new Register((short) 0);
		SP = 0xFFFE;
	}

	private CPU(boolean CGB) {
		this();
		if (CGB) {
			AF.setLow((byte) 0x11);
			PC = 0x100;
		}
	}

	private static int localCount = 1;

	public boolean update() {
		if (PC == 15083) {
			int j = 100; ///заглушка для ловли багов
		}
		//Memory m = Memory.getShared();
		byte opcode = memory.read((short) PC);
		curCode = opname[Byte.toUnsignedInt(opcode)];
		//System.out.printf("PC = %d opcode = %x name = %s\n", PC, opcode, curName);
		curCode.func.exec(curCode.index);
		if (localCount < curCode.duration) {
			//System.out.printf(" -   %02d\n", CPU.localCount);
			localCount += 4;
			return true;
		} else {
			//System.out.printf(" - %s\n", curCode.ass);
			localCount = 4;
			PC++;//= curCode.size;
			return PC < 1000000;//em.getMaxAddress();
		}
	}

	private void nop() {
	}

	private void ld(int arg1, int arg2) {
	}

	public String getAss(){
		return curName;
	}

	public interface Opfunc {
		void exec(int index);
	}

	private static class Opcode {
		int size;
		int duration;
		int index;
		Opfunc func;

		public Opcode(int size, int duration, int index, Opfunc func) {
			this.size = size;
			this.duration = duration;
			this.index = index;
			this.func = func;
		}
	}
}
