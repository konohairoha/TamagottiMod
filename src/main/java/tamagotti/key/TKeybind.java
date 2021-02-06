package tamagotti.key;

import org.lwjgl.input.Keyboard;

public enum TKeybind {

	MODE("ta.key.mode", Keyboard.KEY_V),
	RELODE("ta.key.relode", Keyboard.KEY_R);

	public final String keyName;
	public final int defaultKeyCode;

	TKeybind(String keyName, int defaultKeyCode) {
		this.keyName = keyName;
		this.defaultKeyCode = defaultKeyCode;
	}
}
