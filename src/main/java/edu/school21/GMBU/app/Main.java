package edu.school21.GMBU.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import edu.school21.GMBU.SpringConfig;
import edu.school21.GMBU.emulator.CPU;
import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {



    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringConfig.class);

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + s);

        CPU cpu = context.getBean("CPU", CPU.class);
        Memory memory = context.getBean("memory", Memory.class);
        ClockGenerator cg = new ClockGenerator(cpu,memory);
        cg.loadCartridge();
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Emulator GameBoy");
        cfg.setWindowedMode(640, 480);
        new Thread(cg).start();
        new Lwjgl3Application(new ProgramWindow(cg, cpu), cfg);
    }
}

