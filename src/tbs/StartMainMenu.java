package tbs;

import tbs.mainMenu.MainMenu;

public class StartMainMenu {
	public static void main(String[] args) {
			new Animation("characters/player/player");
			Thread t = new Thread() {
				@Override
				public void run() {
					new GameFrame(new MainMenu(), new Matrix(208, 117), 60, 60).start();
				}
			};
			t.start();
	}
}