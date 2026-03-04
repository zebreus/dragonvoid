package actions;

import tbs.Matrix;
import tbs.Character;

public interface Action {
	public int[][] getPossibleTargets();
	public int[][] getPossibleTargets(int dir);
	public void paint(Matrix mat, int x, int y);
	public void update(long delta);
	public void start(int dir, Character[] victims, int[] victimFields);
	public void stop();
	public boolean getRunning();
}
