package tbs.mainMenu.continueGame;

import tbs.Matrix;
import tbs.mainMenu.MenuButton;
import tbs.mainMenu.MenuItem;

public class SavegamesButton extends MenuButton {

	private int id;

	public SavegamesButton(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent,
			String text, int id) {
		super(x, y, width, height, visible, active, parent, text);
		this.id = id;
	}

	@Override
	public void repaint(Matrix mat) {
		mat.drawSprite(currentSprite, rect.x, rect.y + ((ContinueGameContainer) parent).getOffset(), 1);
		for (int a = 0; a < textSprites.size(); a++) {
			mat.drawDrawable(textSprites.get(a), rect.x + a * (textSprites.getFirst().getWidth() + 1) + 5,
					rect.y + 1 + ((ContinueGameContainer) parent).getOffset());
		}
	}

	@Override
	public void update(long delta) {

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
