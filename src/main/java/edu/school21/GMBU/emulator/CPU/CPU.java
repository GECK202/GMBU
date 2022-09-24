package edu.school21.GMBU.emulator.CPU;

import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class CPU {

	int PC = 4;
	int SP;
	Register AF;
	Register BC;
	Register DE;
	Register HL;

	String curName = "error";
	Opcode curCode;

	@Autowired
	private Memory memory;
	@Autowired
	private Opcodes opcodes;


	public CPU() {
		PC = 0;
		AF = new Register((short) 0);
		BC = new Register((short) 0);
		DE = new Register((short) 0);
		HL = new Register((short) 0);
		SP = 0xFFFE;
	}

	/*
	private CPU(boolean CGB) {
		this();
		if (CGB) {
			AF.setLow((byte) 0x11);
			PC = 0x100;
		}
	}
	*/

	private static int localCount = 1;

	public boolean update() {
		if (PC == 2) {
			int j = 100; ///заглушка для ловли багов
		}
		try {
			int opcode = Byte.toUnsignedInt(memory.read(PC));
			curCode = opcodes.getOpcode(opcode);
			curCode.func.exec();
			System.out.printf("PC = %d opcode = %x name = %s\n", PC, opcode, curName);
			if (localCount < curCode.duration) {
				localCount += 4;
				return true;
			} else {
				localCount = 4;
				return PC < 1000000;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getAss(){
		return curName;
	}
}
