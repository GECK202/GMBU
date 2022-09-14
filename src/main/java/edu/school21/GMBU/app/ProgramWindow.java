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
import edu.school21.GMBU.emulator.CPU;
import edu.school21.GMBU.emulator.Emulator;
import org.springframework.beans.factory.annotation.Autowired;

public class ProgramWindow extends Game implements ApplicationListener, InputProcessor {
    private SpriteBatch batch;
    private BitmapFont font;
    private int currentFrame = 1;
    private boolean flag = false;
    private String clockS = "error";
    private String curOppcode = "error";
    private String maxAddr = "error";

    private String debugMode = "error";
    private String pause = "error";
    private String check = "error";

    private final float updateDelay = 1/30.0f;

    private Pixmap pm;
    private Texture tx;

    private ClockGenerator cg;

    private CPU cpu;

    @Autowired
    public ProgramWindow(ClockGenerator cg, CPU cpu) {
        this.cg = cg;
        this.cpu = cpu;
    }

    @Override
    public void create() {
        ClockGenerator.goFlag=true;
        maxAddr = String.format("%d",100000);//em.getMaxAddress());
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
                        curOppcode = cpu.getAss();
                        debugMode = String.format("%b", cg.getDebugMode());
                        pause = String.format("%b", cg.getPause());
                        check = String.format("%d", cg.getCounter());

                    } catch (Exception e) {
                        //System.err.print(e);
                    }
                }
                pm.setColor(new Color(0,0.5f,0,1));
                for(int i = 0; i<200; i++) {
                    for (int j = 0; j<100; j++) {
                        pm.drawPixel(i,j);
                    }
                }
                pm.setColor(0,1,0,1);
                pm.drawPixel(cg.px,cg.py);
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

        font.draw(batch, "Time:", 5,475);
        font.draw(batch, String.format("%.2f sec", currentFrame*updateDelay), 130, 475);

        font.draw(batch, "Current address:", 5, 450);
        font.draw(batch,  clockS, 130, 450);

        font.draw(batch, "Max address:", 5, 425);
        font.draw(batch, maxAddr, 130, 425);

        font.draw(batch, "Current oppcode:", 5, 400);
        font.draw(batch,  curOppcode, 130, 400);

        font.draw(batch, "debug = ", 300, 475);
        font.draw(batch, debugMode, 355, 475);

        font.draw(batch, "pause = ", 300, 450);
        font.draw(batch, pause, 355, 450);

        font.draw(batch, "check = ", 300, 425);
        font.draw(batch, check, 355, 425);

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
