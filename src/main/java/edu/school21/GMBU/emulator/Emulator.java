package edu.school21.GMBU.emulator;

import edu.school21.GMBU.emulator.CPU.CPU;
import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Emulator {

    @Autowired
   private CPU cpu;

    @Autowired
   private Memory memory;





}
