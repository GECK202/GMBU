package edu.school21.GMBU.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import edu.school21.GMBU.emulator.CPU.CPU;
import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProgramWindow extends Game implements ApplicationListener, InputProcessor {
    private SpriteBatch batch;
    private BitmapFont font;
    private int currentFrame = 1;
    private boolean flag = false;
    private String clockS = "error";
    private String curOpcodeName = "error";
    private String maxAddress = "error";

    private String debugMode = "error";
    private String pause = "error";
    private String check = "error";

    private String mem = "error";

    private final float updateDelay = 1/30.0f;

    private Pixmap pm;
    private Texture tx;

    private final ClockGenerator cg;

    private final CPU cpu;
    private final Memory memory;

    @Autowired
    public ProgramWindow(ClockGenerator cg, CPU cpu, Memory memory) {
        this.cg = cg;
        this.cpu = cpu;
        this.memory = memory;
    }

    private String getMemory(int address) {
        String s = "";
        for (int j = 0; j < 16; j++) {
            for (int i = 0; i < 16; i++){
                s += String.format("%-3X ",Byte.toUnsignedInt(memory.read(address + (j*16) + i)));
            }
            s += "\n";
        }
        return s;
    }

    @Override
    public void create() {
        ClockGenerator.goFlag=true;
        maxAddress = String.format("%d",100000);//em.getMaxAddress());
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.GREEN);
        pm = new Pixmap(200, 100, Pixmap.Format.RGB888);
        pm.setColor(1,1,1,1);
        tx = new Texture(pm);

        Gdx.input.setInputProcessor(this);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                if (currentFrame<0xFFFF) {
                    currentFrame++;
                    flag = true;
                    try {
                        curOpcodeName = cpu.getAss();
                        debugMode = String.format("%b", cg.getDebugMode());
                        pause = String.format("%b", cg.getPause());
                        check = String.format("%d", cg.getCounter());
                        mem = getMemory(0);
                    } catch (Exception e) {
                        //System.err.print(e);
                    }
                }

                for(int i = 0; i<200; i++) {
                    for (int j = 0; j<100; j++) {
                        switch (cg.screen[i][j]) {
                            case 0:
                                pm.setColor(new Color(0,1.0f,0,1));
                                break;
                            case 1:
                                pm.setColor(0,0.8f,0,1);
                                break;
                            case 2:
                                pm.setColor(new Color(0,0.6f,0,1));
                                break;
                            default:
                                pm.setColor(0,0.4f,0,1);
                        }
                        pm.drawPixel(i,j);
                    }
                }

                //pm.drawPixel(cg.px,cg.py);
                tx.draw(pm,0,0);
            }
        },0,updateDelay);
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0.2f, 0.25f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (flag) {
            clockS = String.format("%d", cg.getClockParameter());
            flag = false;
        }
        batch.begin();

        font.draw(batch, "Time:", 5,675);
        font.draw(batch, String.format("%.2f sec", currentFrame*updateDelay), 130, 675);

        font.draw(batch, "Current address:", 5, 650);
        font.draw(batch,  clockS, 130, 650);

        font.draw(batch, "Max address:", 5, 625);
        font.draw(batch, maxAddress, 130, 625);

        font.draw(batch, "Current opcode:", 5, 600);
        font.draw(batch,  curOpcodeName, 130, 600);

        font.draw(batch, "debug = ", 5, 575);
        font.draw(batch, debugMode, 130, 575);

        font.draw(batch, "pause = ", 5, 550);
        font.draw(batch, pause, 130, 550);

        font.draw(batch, "check = ", 5, 525);
        font.draw(batch, check, 130, 525);

        font.draw(batch, mem, 300, 675);

        batch.draw(tx, 10,10, 600, 300);
        batch.end();
    }

    @Override
    public void dispose() {
        ClockGenerator.exitFlag = true;
        pm.dispose();
        tx.dispose();
        batch.dispose();
        font.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.printf("key down %d\n", keycode);
        if (keycode == 131) { Gdx.app.exit(); }
        if (keycode == 62) { cg.invertPause(); }
        if (keycode == 35) { cg.setDebugMode(); }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        System.out.printf("key up %d\n", i);
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        System.out.printf("key tap %c\n", c);
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        System.out.printf("touch down %d %d %d %d\n", i, i1, i2, i3);
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        System.out.printf("touch up %d %d %d %d\n", i, i1, i2, i3);
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        System.out.printf("touch drag %d %d %d\n", i, i1, i2);
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        System.out.printf("mouse moved %d %d\n", i, i1);
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        System.out.printf("scrolled %d\n", i);
        return false;
    }
}
