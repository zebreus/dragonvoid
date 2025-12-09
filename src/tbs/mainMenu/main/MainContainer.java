package tbs.mainMenu.main;

import java.awt.event.KeyEvent;
import java.io.IOException;

import tbs.Matrix;
import tbs.Sprite;
import tbs.mainMenu.MainMenu;
import tbs.mainMenu.MenuButton;
import tbs.mainMenu.MenuContainer;
import tbs.mainMenu.continueGame.ContinueGameContainer;
import tbs.mainMenu.credits.CreditsContainer;
import tbs.mainMenu.newGame.NewGameContainer;
import tbs.mainMenu.options.OptionsContainer;

public class MainContainer extends MenuContainer {

	private static final int NOTHING = 0;
	private static final int UP = 1;
	private static final int DOWN = 2;
	private static final int SELECT = 3;

	private MenuButton[] buttons;
	private int activeButton;
	private ContinueGameButton continueB;
	private NewGameButton newGameB;
	private OptionsButton optionsB;
	private CreditsButton creditsB;
	private ExitButton exitB;

	private ContinueGameContainer continueC;
	private NewGameContainer newGameC;
	private CreditsContainer creditsC;
	private OptionsContainer optionsC;

	private int keystate = NOTHING;

	private long delta;
	private boolean block;

	public MainContainer() {
		super(0, 0, MainMenu.WIDTH, MainMenu.HEIGHT, true, true, null);
		// set background
		try {
			background = new Sprite("mainMenu/background");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// create buttons
		continueB = new ContinueGameButton(12, 20, 80, 9, true, true, this);
		newGameB = new NewGameButton(12, 35, 80, 9, true, true, this);
		optionsB = new OptionsButton(12, 50, 80, 9, true, true, this);
		creditsB = new CreditsButton(12, 65, 80, 9, true, true, this);
		exitB = new ExitButton(12, 95, 80, 9, true, true, this);
		// add buttons to menuitems and buttons array
		menuItems.add(continueB);
		menuItems.add(newGameB);
		menuItems.add(optionsB);
		menuItems.add(creditsB);
		menuItems.add(exitB);
		buttons = new MenuButton[] { continueB, newGameB, optionsB, creditsB, exitB };
		// set starting conditions
		continueB.hover();
		activeButton = 0;
		delta = 0;

		// create container
		continueC = new ContinueGameContainer(false, false, this);
		newGameC = new NewGameContainer(false, false, this);
		creditsC = new CreditsContainer(false, false, this);
		optionsC = new OptionsContainer(false, false, this);
		// add container to menuitems
		menuItems.add(continueC);
		menuItems.add(newGameC);
		menuItems.add(creditsC);
		menuItems.add(optionsC);
	}

	@Override
	public void update(long delta) {
		if (this.delta > 100) {
			if (!block) {
				switch (keystate) {
				case DOWN:
					if (activeButton < buttons.length - 1) {
						buttons[activeButton].unhover();
						activeButton++;
						buttons[activeButton].hover();
					}
					break;
				case UP:
					if (activeButton > 0) {
						buttons[activeButton].unhover();
						activeButton--;
						buttons[activeButton].hover();
					}
					break;
				case SELECT:
					buttonClicked();
					break;

				default:
					break;
				}
				keystate = NOTHING;
			} else {
				keystate = NOTHING;
			}
			this.delta = 0;
		} else {
			this.delta += delta;
		}
	}

	private void buttonClicked() {
		buttons[activeButton].select();
		if (buttons[activeButton] == continueB) {
			continueC.show();
			continueC.activate();
		} else if (buttons[activeButton] == newGameB) {
			newGameC.show();
			newGameC.activate();
		} else if (buttons[activeButton] == creditsB) {
			creditsC.show();
			creditsC.activate();
		} else if (buttons[activeButton] == optionsB) {
			optionsC.show();
			optionsC.activate();
		} else if (buttons[activeButton] == exitB) {
			// TODO change
			System.exit(0);
		}
		block = true;
	}

	public void backToMain(MenuContainer container) {
		block = false;
		container.deactivate();
		container.hide();
		buttons[activeButton].hover();
	}

	@Override
	public void repaint(Matrix mat) {
		mat.drawSprite(background, 0, 0, 1);
	}

	@Override
	public void keyPressed(int keycode) {
		switch (keycode) {
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
		if (!block) {
			for (int a = 0; a < buttons.length; a++) {
				if (buttons[a].getRect().contains(x, y)) {
					keystate = SELECT;
					break;
				}
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
	}

	@Override
	public void mouseMove(int x, int y) {
		if (!block) {
			for (int a = 0; a < buttons.length; a++) {
				if (buttons[a].getRect().contains(x, y)) {
					for (MenuButton b : buttons) {
						b.unhover();
					}
					buttons[a].hover();
					activeButton = a;
					keystate = NOTHING;
					break;
				}
			}
		}
	}

	@Override
	public void mouseLeftClick(int x, int y) {
	}

	@Override
	public void mouseRightClick(int x, int y) {
	}

}
