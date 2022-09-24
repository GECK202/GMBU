package edu.school21.GMBU.emulator.CPU;

public class Register {
    private short reg;

    public Register() {
        this.reg = (short)0;
    }

    public Register(short reg) {
        this.reg = reg;
    }

    public byte getHigh() {
        return (byte)(reg>>>8);
    }

    public byte getLow() {
        return (byte)(reg&0xFF);
    }

    public void setHigh(byte value) { reg = (short) ((short)value << 8 | (reg & 0xFF)); }

    public void setLow(byte value) { reg = (short) ((short)value | (reg & 0xFF00)); }

    public byte getByte(RegisterByte rb) {
        if (rb == RegisterByte.HIGH) { return getHigh(); }
        return getLow();
    }

    public void setByte(RegisterByte rb, byte value) {
        if (rb == RegisterByte.HIGH) { setHigh(value); }
        else { setLow(value); }
    }

    public void set(Short reg) {
        this.reg = reg;
    }

    public short get() {
        return reg;
    }

    public void setBit(int position) {
        if (position >=0 && position < 16) { reg = (short) (reg | (1 << position)); }
    }

    public void flushBit(int position) {
        if (position >= 0 && position < 16) { reg = (short) (reg & ~(short)(1 << position)); }
    }

    public void setBit(boolean bit, int position) {
        if (bit) { setBit(position); }
        else { flushBit(position); }
    }

    public boolean getBit(int position) {
        if (position >= 0 && position < 16) { return ((reg >> position) & 1) != 0; }
        return false;
    }

    public void setBitByte(RegisterByte rb, boolean bit, int position) {
        if (rb == RegisterByte.HIGH) { setBit(bit, position + 8); }
        else { setBit(bit, position); }
    }

    public boolean getBitByte(RegisterByte rb, int position) {
        if (rb == RegisterByte.HIGH) { return getBit(position + 8); }
        return getBit(position);
    }

}
