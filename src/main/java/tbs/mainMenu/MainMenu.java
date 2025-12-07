package tbs.mainMenu;

import tbs.FrameHook;
import tbs.Matrix;
import tbs.mainMenu.main.MainContainer;

public class MainMenu implements FrameHook {

	public static final int WIDTH = 208;
	public static final int HEIGHT = 117;

	private MainContainer mainContainer;

	public MainMenu() {
		mainContainer = new MainContainer();
	}

	@Override
	public void update(long delta) {
		mainContainer.udate(delta);
	}

	@Override
	public void repaint(Matrix mat) {
		mainContainer.rpaint(mat);
	}

	@Override
	public void keyPressed(int keycode) {
		mainContainer.kPressed(keycode);
	}

	@Override
	public void keyReleased(int keycode) {
		mainContainer.kReleased(keycode);
	}

	@Override
	public void keyTyped(int keycode) {
		mainContainer.kTyped(keycode);
	}

	@Override
	public void mouseLeftPress(int x, int y) {
		mainContainer.mLeftPressed(x, y);
	}

	@Override
	public void mouseLeftRelease(int x, int y) {
		mainContainer.mLeftRelease(x, y);
	}

	@Override
	public void mouseRightPress(int x, int y) {
		mainContainer.mRightPress(x, y);
	}

	@Override
	public void mouseRightRelease(int x, int y) {
		mainContainer.mRightRelease(x, y);
	}

	@Override
	public void mouseMove(int x, int y) {
		mainContainer.mMove(x, y);
	}

	@Override
	public void mouseLeftClick(int x, int y) {
		mainContainer.mLeftClick(x, y);
	}

	@Override
	public void mouseRightClick(int x, int y) {
		mainContainer.mRightClick(x, y);
	}

}
