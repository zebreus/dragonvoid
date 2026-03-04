package tbs.mainMenu;

import java.io.IOException;

import tbs.Sprite;

public abstract class MenuContainer extends MenuItem {

	protected Sprite background;

	public MenuContainer(int x, int y, int width, int height, boolean visible, boolean active, MenuItem parent) {
		super(x, y, width, height, visible, active, parent);
		try {
			this.background = new Sprite("mainMenu/empty");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setBackground(Sprite background) {
		this.background = background;
	}

	public Sprite getBackground() {
		return background;
	}

}
