package edu.school21.GMBU.emulator.CPU;

public class Opcode {

    int size;
    int duration;
    OpcodeFunc func;

    public Opcode(int size, int duration, OpcodeFunc func) {
        this.size = size;
        this.duration = duration;
        this.func = func;
    }

}
