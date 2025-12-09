package tbs.mainMenu.newGame;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;

import tbs.GameFrame;
import tbs.Matrix;
import tbs.SaveFeedbackReceiver;
import tbs.SaveManager;
import tbs.Sprite;
import tbs.StartArenaMode;
import tbs.WorldLoadingScreen;
import tbs.mainMenu.ActionButton;
import tbs.mainMenu.ActionListener;
import tbs.mainMenu.InputField;
import tbs.mainMenu.Label;
import tbs.mainMenu.MainMenu;
import tbs.mainMenu.MenuContainer;
import tbs.mainMenu.MenuItem;
import tbs.mainMenu.main.MainContainer;

public class NewGameContainer extends MenuContainer implements ActionListener, SaveFeedbackReceiver {

	private LinkedList<Label> difficulties;
	private int currentDifficulty;
	private NewgameButton newgameButton;
	private InputField nameField;

	public NewGameContainer(boolean visible, boolean active, MenuItem parent) {
		super(MainMenu.WIDTH / 2, 0, MainMenu.WIDTH / 2, MainMenu.HEIGHT, visible, active, parent);
		try {
//			setBackground(new Sprite("mainMenu/backgroundRight"));
			setBackground(new Sprite("mainMenu/empty"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		nameField = new InputField(MainMenu.WIDTH / 2 + 12, 35, 80, 9, true, true, this, "", 12);
		menuItems.add(nameField);
		difficulties = new LinkedList<Label>();
		for (String s : new String[] { "brutal", "hoellisch", "schwer", "unschaffbar", "insane", "goettlich",
				"kamikaze", "gefaehrlich", "fantastisch", "spartanisch" }) {
			difficulties.add(new Label(MainMenu.WIDTH / 2 + 12, 50, 80, 9, visible, active, parent, s));
		}
		for (Label l : difficulties) {
			menuItems.add(l);
		}
		menuItems.add(new ActionButton(MainMenu.WIDTH / 2 + 1, 50, 9, 9, true, true, this, ActionButton.BACK, this));
		menuItems.add(new ActionButton(MainMenu.WIDTH - 10, 50, 9, 9, true, true, this, ActionButton.FORWARD, this));
		newgameButton = new NewgameButton(this);
		menuItems.add(newgameButton);
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
		switch (keycode) {
		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_LEFT:
			((MainContainer) parent).backToMain(this);
			break;
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
		if (newgameButton.getRect().contains(x, y) && !nameField.getText().isEmpty()) {
			newgameButton.hover();
		} else {
			newgameButton.unhover();
		}
	}

	@Override
	public void mouseLeftClick(int x, int y) {
		if (newgameButton.getRect().contains(x, y) && !nameField.getText().trim().isEmpty()) {
			try {
				SaveManager.createSave(nameField.getText(), currentDifficulty, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseRightClick(int x, int y) {

	}

	@Override
	public void action(int type) {
		if (type == ActionButton.FORWARD && currentDifficulty < difficulties.size() - 1) {
			currentDifficulty++;
		} else if (type == ActionButton.BACK && currentDifficulty > 0) {
			currentDifficulty--;
		}
		updateDifficulties();
	}

	private void updateDifficulties() {
		for (Label l : difficulties) {
			l.hide();
		}
		difficulties.get(currentDifficulty).show();
	}

	@Override
	public void show() {
		super.show();
		updateDifficulties();
	}

	@Override
	public void saveLoadingProgress(int percentage) {

	}

	@Override
	public void saveLoaded() {

	}

	@Override
	public void savingProgress(int percentage) {

	}

	@Override
	public void saved() {

	}

	@Override
	public void creatingError() {
		System.err.println("couldnt create save");
	}

	@Override
	public void creatingProgress(int percentage) {
		System.out.println(percentage + "% loaded");
	}

	@Override
	public void created() {
		try {
			SaveManager saves = new SaveManager(nameField.getText(), new StartArenaMode());
			saves.loadSave();
			// TODO Sauschlechten Code ausbessern
			final WorldLoadingScreen wls = new WorldLoadingScreen((String) saves.getProperty("worldname").getContent(),
					saves);
			GameFrame.getFrame().setHook(wls);
			new Thread() {
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						wls.loadWorld();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();

		} catch (Exception e) {
			System.err.println("Unable to switch to world loading screen");
			e.printStackTrace();
		}
	}

}
