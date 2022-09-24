package edu.school21.GMBU.emulator.CPU;

import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Opcodes {
    @Autowired
    private CPU cpu;
    @Autowired
    private Memory memory;
    private final Opcode[] opcodes = new Opcode[256];

    public Opcode getOpcode(int index) throws Exception {
        if (index >= 0 && index < 256) {
            return opcodes[index];
        } else {
            System.out.println("error");
            throw new Exception();
        }
    }

    @PostConstruct
    private void init() {
        opcodes[0x00] = new Opcode(1, 4, ()-> {
            cpu.curName = "0x00: NOP";
            cpu.PC++;
        });
        opcodes[0x01] = new Opcode(3, 12, ()-> {
            //Memory m = Memory.getShared();
            cpu.curName = "0x01: LD BC,d16";
            byte srcL = memory.read(cpu.PC);
            cpu.PC++;
            byte srcH = memory.read(cpu.PC);
            cpu.PC++;
            LDHL(cpu.BC, srcL, srcH);
            cpu.PC++;
        });
        opcodes[0x02] = new Opcode(1, 8, ()-> {
            short address = memory.read(cpu.BC.get());
            cpu.curName = "0x02: LD (BC), A";
            LDM(address, cpu.AF.getHigh());
            cpu.PC++;
        });
        opcodes[0x03] = new Opcode(1,8, ()-> {
            cpu.curName = "0x03: INC BC";
            INCR(cpu.BC);
            cpu.PC++;
        });
        opcodes[0x04] = new Opcode(1,4, ()-> {
            cpu.curName = "0x04: INC B";
            INCRB(cpu.BC, RegisterByte.HIGH);
            cpu.PC++;
        });
        opcodes[0x05] = new Opcode(1,4, ()-> {
            cpu.curName = "0x05: DEC B";
            DECRB(cpu.BC, RegisterByte.HIGH);
            cpu.PC++;
        });
        opcodes[0x06] = new Opcode(2, 8, ()-> {
            //Memory m = Memory.getShared();
            cpu.curName = "0x06: LD B d8";
            byte value = memory.read(cpu.PC);
            cpu.PC++;
            LD(cpu.BC, RegisterByte.HIGH, value);
            cpu.PC++;
        });
        opcodes[0x07] = new Opcode(1,4, ()-> {
            cpu.curName = "0x07: RLCA";
            byte a = cpu.AF.getHigh();
            boolean b7 = getBit(a, 7);
            setZFlag(false);
            setHFlag(false);
            setNFlag(false);
            setCFlag(b7);
            cpu.AF.setHigh((byte)(a<<1));
            cpu.AF.setBitByte(RegisterByte.HIGH, b7, 0);
            cpu.PC++;
        });
        opcodes[0x08] = new Opcode(3,20, ()-> {
            cpu.curName = "0x08: LD (a16) SP";
            short value = memory.read(cpu.PC);
            cpu.PC++;
            value |= ((short)memory.read(cpu.PC))<<8;
            cpu.PC++;
            memory.writeWord(cpu.SP, value);
            cpu.PC++;
        });
        opcodes[0x09] = new Opcode(1, 8, ()-> {
            cpu.curName = "0x09: ADD HL BC";
            int op1 = cpu.HL.get();
            int op2 = cpu.BC.get();
            int result = op1 + op2;
            boolean cFlag = (result >> 16) != 0;
            setHFlag((short) op1, (short) op2, (short) result);
            setNFlag(false);
            setCFlag(cFlag);
            cpu.PC++;
        });
        opcodes[0x0A] = new Opcode(1,8, ()-> {
            cpu.curName = "0x0A: LD A, (BC)";
            cpu.AF.setHigh((byte) memory.readWord(cpu.BC.get()));
            cpu.PC++;
        });
        opcodes[0x0B] = new Opcode(1,8, ()-> {
            cpu.curName = "0x0B: DEC BC";
            DECR(cpu.BC);
            cpu.PC++;
        });
        opcodes[0x0C] = new Opcode(1,4, ()->{
            cpu.curName = "0x0C: INC C";
            INCRB(cpu.BC, RegisterByte.LOW);
            cpu.PC++;
        });
        opcodes[0x0D] = new Opcode(1,4, ()-> {
            cpu.curName = "0x0D: DEC C";
            DECRB(cpu.BC, RegisterByte.LOW);
            cpu.PC++;
        });
        opcodes[0x0E] = new Opcode(2, 8, ()-> {
            cpu.curName = "0x0E: LD C d8";
            byte value = memory.read(cpu.PC);
            cpu.PC++;
            LD(cpu.BC, RegisterByte.LOW, value);
            cpu.PC++;
        });
        opcodes[0x0F] = new Opcode(1,4, ()-> {
            cpu.curName = "0x0F: RRCA";
            byte a = cpu.AF.getHigh();
            boolean b0 = getBit(a, 0);
            setZFlag(false);
            setHFlag(false);
            setNFlag(false);
            setCFlag(b0);
            cpu.AF.setHigh((byte)(a>>1));
            cpu.AF.setBitByte(RegisterByte.HIGH, b0, 7);
            cpu.PC++;
        });


        for (int i = 0x10; i < 256; i++) {
            int finalI = i;
            opcodes[i] = new Opcode(1, 4, ()-> {
                cpu.curName = String.format("0x%02x: COMMAND", finalI);
                cpu.PC++;
            });
        }
    }

    public boolean getBit(byte bt, int position) {
        if (position >= 0 && position < 8) { return ((bt >> position) & 1) != 0; }
        return false;
    }

    private void setHFlag(boolean flag) {
        cpu.AF.setBit(flag, 5);
    }

    private void setHFlag(byte op1, byte op2, byte result) {
        boolean resBit = getBit(result, 4);
        if (getBit(op1, 4) == getBit(op2, 4)) {
            cpu.AF.setBit(resBit, 5);
        } else {
            cpu.AF.setBit(!resBit, 5);
        }
    }

    private void setHFlag(short op1, short op2, short result) {
        setHFlag((byte)(op1 >> 8), (byte)(op2 >> 8), (byte)(result >> 8));
    }

    private void setZFlag(boolean flag) {
        cpu.AF.setBit(flag, 7);
    }

    private void setZFlag(byte result) {
        cpu.AF.setBit((result == (byte)0), 7);
    }

    private void setNFlag(boolean flag) {
        cpu.AF.setBit(flag, 6);
    }

    private void setCFlag(boolean flag) {
        cpu.AF.setBit(flag, 4);
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
}
