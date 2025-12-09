package tbs.mainMenu.options;

import java.io.IOException;
import java.util.LinkedList;

import tbs.Matrix;
import tbs.Sprite;
import tbs.mainMenu.ActionButton;
import tbs.mainMenu.ActionListener;
import tbs.mainMenu.Label;
import tbs.mainMenu.MainMenu;
import tbs.mainMenu.MenuContainer;
import tbs.mainMenu.MenuItem;
import tbs.mainMenu.main.MainContainer;

public class OptionsContainer extends MenuContainer implements ActionListener {

	private LinkedList<Label> volumes;
	private int currentVolume;

	public OptionsContainer(boolean visible, boolean active, MenuItem parent) {
		super(MainMenu.WIDTH / 2, 0, MainMenu.WIDTH / 2, MainMenu.HEIGHT, visible, active, parent);
		try {
//			setBackground(new Sprite("mainMenu/backgroundRight"));
			setBackground(new Sprite("mainMenu/empty"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		volumes = new LinkedList<Label>();
		for (String s : new String[] { "leise", "mittel", "laut" }) {
			volumes.add(new Label(MainMenu.WIDTH / 2 + 12, 50, 80, 9, visible, active, parent, s));
		}
		for (Label l : volumes) {
			menuItems.add(l);
		}
		menuItems.add(new ActionButton(MainMenu.WIDTH / 2 + 1, 50, 9, 9, true, true, this, ActionButton.BACK, this));
		menuItems.add(new ActionButton(MainMenu.WIDTH - 10, 50, 9, 9, true, true, this, ActionButton.FORWARD, this));
	}

	@Override
	public void update(long delta) {

	}

	@Override
	public void repaint(Matrix mat) {
		mat.drawDrawable(background, rect.x, rect.y);

	}

	@Override
	public void keyPressed(int keycode) {

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

	}

	@Override
	public void mouseRightRelease(int x, int y) {
		((MainContainer) parent).backToMain(this);
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

	@Override
	public void action(int type) {
		if (type == ActionButton.FORWARD && currentVolume < volumes.size() - 1) {
			currentVolume++;
		} else if (type == ActionButton.BACK && currentVolume > 0) {
			currentVolume--;
		}
		updateDifficulties();
	}

	private void updateDifficulties() {
		for (Label l : volumes) {
			l.hide();
		}
		volumes.get(currentVolume).show();
	}

	@Override
	public void show() {
		super.show();
		updateDifficulties();
	}

}
