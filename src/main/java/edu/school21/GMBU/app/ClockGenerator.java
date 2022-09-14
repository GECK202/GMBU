package edu.school21.GMBU.app;

import edu.school21.GMBU.emulator.CPU;
import edu.school21.GMBU.emulator.Emulator;
import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClockGenerator implements Runnable {

    //private static ClockGenerator shared = null;

    private long secForMillion = 0;
    private int clockParameter = 0;
    private boolean exitFlag = false;

    private boolean pause = false;
    private boolean debugMode = false;
    private boolean debugGo = true;
    private int nnn = 0;

    public int px = 1;
    public int py = 1;
    private int dx = 1;
    private int dy = 2;


    private CPU cpu;
    private Memory memory;

    @Autowired
    public ClockGenerator(CPU cpu, Memory memory) {
        this.secForMillion = 1200/20;
        this.cpu = cpu;
        this.memory = memory;
    }

    public void loadCartridge() {
        String filename = "D://Downloads//Army_Men_2//Army.gbc";
        memory.loadCartridge(filename);
    }


    public int getClockParameter() {
        return clockParameter;
    }

    public void setExitFlag() {
        exitFlag = true;
    }

    public boolean getPause() { return pause; }

    public boolean getDebugMode() { return debugMode; }

    public int getNnn() { return nnn; }

    public void invertPause() {
        if (debugMode) {
            debugMode = false;
            pause = true;
        }
        pause = !pause;
    }

    public void setDebugMode() {
        debugMode = true;
        debugGo = true;
        pause = false;
    }

    @Override
    public void run() {
        System.out.printf("IN NEW THREAD");
        boolean flag;

        while (!exitFlag) {
            nnn += 1;
            //System.out.printf("clock = %d, debug = %b, pause = %b \n", clockParameter, debugMode, pause);
            if (secForMillion > 0 && !pause) {

                if (!debugMode || (debugMode && debugGo)) {
                    clockParameter++;
                    flag = cpu.update();

                    if (clockParameter % 1000 == 0) {
                        px = px + dx;
                        py = py + dy;
                        dx = px < 199 ? px > 1 ? dx : -dx : -dx;
                        dy = py < 99 ? py > 1 ? dy : -dy : -dy;
                    }

                    exitFlag = !flag;
                    debugGo = false;
                }
            }
            if (clockParameter%secForMillion == 0 || debugMode) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
