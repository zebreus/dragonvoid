package tbs.mainMenu.credits;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

import tbs.Matrix;
import tbs.mainMenu.Label;
import tbs.mainMenu.MainMenu;
import tbs.mainMenu.MenuContainer;
import tbs.mainMenu.MenuItem;
import tbs.mainMenu.main.MainContainer;

public class CreditsContainer extends MenuContainer {

	private LinkedList<String> text;

	public CreditsContainer(boolean visible, boolean active, MenuItem parent) {
		super(MainMenu.WIDTH / 2, 0, MainMenu.WIDTH / 2, MainMenu.HEIGHT, visible, active, parent);
		text = new LinkedList<String>();
		text.add("DEATHMATCH");
		text.add("");
		text.add("coding");
		text.add("lennart");
		text.add("jan");
		text.add("janek");
		text.add("");
		text.add("graphics");
		text.add("fynn");
		text.add("leon");
		text.add("johannes");
		text.add("");
		text.add("danke merkel");
		for (int i = 0; i < text.size(); i++) {
			menuItems.add(new Label(MainMenu.WIDTH / 2 + 12, 0 + i * 9, 80, 9, true, true, this, text.get(i)));
		}
	}

	@Override
	public void update(long delta) {
	}

	@Override
	public void repaint(Matrix mat) {
	}

	@Override
	public void keyPressed(int keycode) {
		if (keycode == KeyEvent.VK_ESCAPE || keycode == KeyEvent.VK_LEFT) {
			((MainContainer) parent).backToMain(this);
		}
	}

	@Override
	public void keyReleased(int keycode) {
	}

	@Override
	public void keyTyped(int keycode) {
	}

	@Override
	public void mouseLeftPress(int x, int y) {
	}

	@Override
	public void mouseLeftRelease(int x, int y) {
	}

	@Override
	public void mouseRightPress(int x, int y) {
		((MainContainer) parent).backToMain(this);
	}

	@Override
	public void mouseRightRelease(int x, int y) {
	}

	@Override
	public void mouseMove(int x, int y) {
	}

	@Override
	public void mouseLeftClick(int x, int y) {
	}

	@Override
	public void mouseRightClick(int x, int y) {
	}

}
