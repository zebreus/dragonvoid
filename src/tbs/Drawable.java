package tbs;

import java.awt.Color;

public interface Drawable {
public int[][] getAlpha();
public Color[][] getColors();
public int getWidth();
public int getHeight();
public void paint(Matrix mat, int x, int y);
public String getName();
}
