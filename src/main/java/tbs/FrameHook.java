package tbs;

public interface FrameHook {
public abstract void update(long delta);
public abstract void repaint(Matrix mat);
public abstract void keyPressed(int keycode);
public abstract void keyReleased(int keycode);
public abstract void keyTyped(int keycode);
public abstract void mouseLeftPress(int x,int y);
public abstract void mouseLeftRelease(int x, int y);
public abstract void mouseRightPress(int x,int y);
public abstract void mouseRightRelease(int x, int y);
public abstract void mouseMove(int x, int y);
public abstract void mouseLeftClick(int i, int j);
public abstract void mouseRightClick(int i, int j);
}
