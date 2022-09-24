package edu.school21.GMBU.config;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import edu.school21.GMBU.app.ClockGenerator;
import edu.school21.GMBU.app.ProgramWindow;
import edu.school21.GMBU.emulator.CPU.CPU;
import edu.school21.GMBU.emulator.Memory.Memory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("edu.school21.GMBU")
@PropertySource("classpath:application.properties")
public class SpringConfig {
    @Bean
    public Lwjgl3Application lwjgl3Application(ProgramWindow programWindow) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Emulator GameBoy");
        cfg.setWindowedMode(800, 700);
        return new Lwjgl3Application(programWindow, cfg);
    }
}
