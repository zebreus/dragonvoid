package tbs.mainMenu.continueGame;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import tbs.GameFrame;
import tbs.Matrix;
import tbs.SaveManager;
import tbs.Sprite;
import tbs.StartArenaMode;
import tbs.WorldLoadingScreen;
import tbs.mainMenu.ActionButton;
import tbs.mainMenu.ActionListener;
import tbs.mainMenu.MainMenu;
import tbs.mainMenu.MenuButton;
import tbs.mainMenu.MenuContainer;
import tbs.mainMenu.MenuItem;
import tbs.mainMenu.main.MainContainer;

public class ContinueGameContainer extends MenuContainer implements ActionListener {

	private static final int NOTHING = 0;
	private static final int UP = 1;
	private static final int DOWN = 2;
	private static final int SELECT = 3;

	private String[] saveGames;
	private LinkedList<SavegamesButton> saveGamesButtons;

	private int activeButton;
	private int keystate = NOTHING;

	private long delta;

	private int offset;

	public ContinueGameContainer(boolean visible, boolean active, MenuItem parent) {
		super(MainMenu.WIDTH / 2, 0, MainMenu.WIDTH / 2, MainMenu.HEIGHT, visible, active, parent);
		try {
//			setBackground(new Sprite("mainMenu/backgroundRight"));
			setBackground(new Sprite("mainMenu/empty"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		menuItems.add(
				new ActionButton(MainMenu.WIDTH - (12 + 2 * 9 + 5), 5, 9, 9, true, true, this, ActionButton.UP, this));
		menuItems.add(new ActionButton(MainMenu.WIDTH - (12 + 9), 5, 9, 9, true, true, this, ActionButton.DOWN, this));

		loadGames();
		saveGamesButtons.getFirst().hover();
		activeButton = 0;
		delta = 0;
		offset = 0;
	}

	private boolean loadGames() {
		try {
			saveGames = SaveManager.listSavegames();
			if (saveGames.length == 0) {
				saveGames = new String[] { "Leer" };
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		saveGamesButtons = new LinkedList<SavegamesButton>();
		System.out.println(saveGames.length + " " +Arrays.toString(saveGames) + " cheers");
		for (int a = 0; a < saveGames.length; a++) {
			System.out.println("savegames " + saveGames[a] + " " + a);
			saveGamesButtons.add(new SavegamesButton(MainMenu.WIDTH / 2 + 12, 20 + a * 15, 80, 9, true, true, this,
					saveGames[a], a));
		}
		for (MenuButton s : saveGamesButtons) {
			menuItems.add(s);
		}
		return true;
	}

	@Override
	public void update(long delta) {
		if (this.delta > 100) {
			switch (keystate) {
			case DOWN:
				if (activeButton < saveGamesButtons.size() - 1) {
					saveGamesButtons.get(activeButton).unhover();
					activeButton++;
					saveGamesButtons.get(activeButton).hover();
					if (activeButton > offset + 5) {
						action(ActionButton.DOWN);
					}
				}
				break;
			case UP:
				if (activeButton > 0) {
					saveGamesButtons.get(activeButton).unhover();
					activeButton--;
					saveGamesButtons.get(activeButton).hover();
					if (activeButton < offset) {
						action(ActionButton.UP);
					}
				}
				break;
			case SELECT:
				buttonClicked();
				break;

			default:
				break;
			}
			keystate = NOTHING;
			this.delta = 0;
		} else {
			this.delta += delta;
		}
	}

	private void buttonClicked() {
		saveGamesButtons.get(activeButton).select();
		try {
			SaveManager saves = new SaveManager(saveGamesButtons.get(activeButton).getText(), new StartArenaMode());
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
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			keystate = DOWN;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			keystate = UP;
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_RIGHT:
			keystate = SELECT;
			break;
		default:
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
		for (int a = 0; a < saveGamesButtons.size(); a++) {
			if (saveGamesButtons.get(a).getRect().contains(x, y)) {
				keystate = SELECT;
				break;
			}
		}
	}

	@Override
	public void mouseLeftRelease(int x, int y) {
		keystate = NOTHING;
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
		for (int a = 0; a < saveGamesButtons.size(); a++) {
			if (saveGamesButtons.get(a).getRect().contains(x, y)) {
				for (MenuButton b : saveGamesButtons) {
					b.unhover();
				}
				saveGamesButtons.get(a).hover();
				activeButton = a;
				keystate = NOTHING;
				break;
			}
		}
	}

	@Override
	public void mouseLeftClick(int x, int y) {
	}

	@Override
	public void mouseRightClick(int x, int y) {
	}

	public int getOffset() {
		return -15 * offset;
	}

	@Override
	public void action(int type) {
		switch (type) {
		case ActionButton.UP:
			if (offset > 0) {
				offset--;
			}
			break;
		case ActionButton.DOWN:
			if (offset < saveGamesButtons.size() - 6) {
				offset++;
			}
			break;
		default:
			break;
		}
		for (SavegamesButton b : saveGamesButtons) {
			b.show();
			if (b.getId() < offset || b.getId() > offset + 5) {
				b.hide();
			}
		}
	}

	@Override
	public void show() {
		super.show();
		// hide not visible buttons
		for (SavegamesButton b : saveGamesButtons) {
			b.show();
			if (b.getId() < offset || b.getId() > offset + 5) {
				b.hide();
			}
		}
	}

}
