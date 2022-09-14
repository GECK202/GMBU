package edu.school21.GMBU.emulator.Memory;

public enum EmulationModes {
	EMULMODE_FORCEDMG(0),
	EMULMODE_FORCECGB(1),
	EMULMODE_AUTO(2);

	private int emulmode;

	private EmulationModes(int emulmode) {
		this.emulmode = emulmode;
	}
}
