package edu.school21.GMBU.app;

import edu.school21.GMBU.emulator.CPU.CPU;
import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClockGenerator implements Runnable {

    //private static ClockGenerator shared = null;

    private final long secForMillion;
    private int clockParameter = 0;
    public static boolean exitFlag = false;
    public static boolean goFlag = false;


    private boolean pause = true;
    private boolean debugMode = false;
    private boolean debugGo = true;
    private int counter = 0;
    private int px = 1;
    private int py = 1;
    private int dx = 1;
    private int dy = 2;

    public int[][] screen = new int[200][100];


    private final CPU cpu;
    private final Memory memory;

    @Autowired
    public ClockGenerator(CPU cpu, Memory memory) {
        this.secForMillion = 60;
        this.cpu = cpu;
        this.memory = memory;
        loadCartridge();
        new Thread(this).start();
    }

    public void loadCartridge() {
        String filename = ".//roms//Army_Men_2//Army.gbc";
        memory.loadCartridge(filename);
    }


    public int getClockParameter() {
        return clockParameter;
    }

    public boolean getPause() { return pause; }

    public boolean getDebugMode() { return debugMode; }

    public int getCounter() { return counter; }

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

    private void clearScreen() {
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 100; j++) {
                screen[i][j] = 3;
            }
        }
    }
    private void drawPause(int x, int y) {

        for (int i = x; i < x + 70; i++){
            for (int j = y; j < y + 30; j++) {
                screen[i][j] = 2;
            }
        }

        for (int j = 1; j < 11; j++) {
            screen[x+10][y+10+j] = 0;
            screen[x+20][y+10+j] = 0;
            screen[x+26][y+10+j] = 0;
            screen[x+50][y+10+j] = 0;
        }
        for (int j = 0; j < 10; j++) {
            screen[x+30][y+10+j] = 0;
            screen[x+36][y+10+j] = 0;
        }
        for (int j = 1; j < 5; j++) {
            screen[x+16][y+10+j] = 0;
            screen[x+40][y+10+j] = 0;
            screen[x+46][y+15+j] = 0;
        }
        for (int i=1; i < 6; i++) {
            screen[x+10+i][y+10] = 0;
            screen[x+10+i][y+15] = 0;
            screen[x+20+i][y+10] = 0;
            screen[x+20+i][y+15] = 0;
            screen[x+30+i][y+20] = 0;
            screen[x+40+i][y+10] = 0;
            screen[x+40+i][y+15] = 0;
            screen[x+40+i][y+20] = 0;
            screen[x+50+i][y+10] = 0;
            screen[x+50+i][y+15] = 0;
            screen[x+50+i][y+20] = 0;
        }
    }
    private void drawBall() {
        px = px + dx;
        py = py + dy;
        dx = px < 199 ? px > 1 ? dx : -dx : -dx;
        dy = py < 99 ? py > 1 ? dy : -dy : -dy;
        for(int i = 0; i<200; i++) {
            for (int j = 0; j<100; j++) {
                screen[i][j] = 3;
                if (px > i - 5 && px < i + 5 && py > j - 5 && py < j + 5) {
                    screen[i][j] = 2;
                }
                if (px > i - 3 && px < i + 3 && py > j - 3 && py < j + 3) {
                    screen[i][j] = 1;
                }
                if (px > i - 1 && px < i + 1 && py > j - 1 && py < j + 1) {
                    screen[i][j] = 0;
                }
            }
        }
    }

    @Override
    public void run() {
        System.out.println("IN NEW THREAD");
        boolean flag;
        clearScreen();
        drawPause(0,0);
        while (!ClockGenerator.exitFlag) {
            System.out.println(counter);
                counter++;
                //System.out.printf("clock = %d, debug = %b, pause = %b \n", clockParameter, debugMode, pause);
                if (secForMillion > 0 && !pause) {
                    if (!debugMode || (debugMode && debugGo)) {
                        clockParameter++;
                        flag = cpu.update();
                        if (clockParameter % 1000 == 0) {
                            drawBall();
                        }
                        ClockGenerator.exitFlag = !flag;
                        debugGo = false;
                    }
                } else if (secForMillion > 0 && pause) {
                    drawPause(0,0);
                }
                if (clockParameter % secForMillion == 0 || debugMode) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    }
}
