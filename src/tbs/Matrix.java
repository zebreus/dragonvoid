package tbs;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Matrix {
/**
 * @uml.property  name="matrix" multiplicity="(0 -1)" dimension="2"
 */
Color[][] matrix;
/**
 * @uml.property  name="width"
 */
int width;
/**
 * @uml.property  name="height"
 */
int height;
public Matrix(int width,int height){
	this.width=width;
	this.height=height;
	matrix = new Color[width][height];
	for(int x=0;x<width;x++){
		for(int y=0;y<height;y++){
			matrix[x][y]=new Color(0,0,255);
		}
	}
}
/** draws sprite at given position
 * @param d Sprite that gest drawn
 * @param x x position
 * @param y y position
 */
public void drawDrawable(Drawable d,int x,int y){
	for(int posX=x;posX<x+d.getWidth();posX++){
		for(int posY=y;posY<y+d.getHeight();posY++){
			if(d.getAlpha()[posX-x][posY-y]==255){
				if(posX>=0&&posX<width&&posY>=0&&posY<height){
					matrix[posX][posY]=d.getColors()[posX-x][posY-y];
				}
			}
		}
	}
}
/** draws sprite at given position with transparency
 * @param s Sprite that gest drawn
 * @param x x position
 * @param y y position
 * @param alpha transparency between 0(transparent) and 100(opaque)
 */
public void drawSprite(Sprite s,int x,int y,int alpha){
	for(int posX=x;posX<x+s.width;posX++){
		for(int posY=y;posY<y+s.height;posY++){
			if(posX>=0&&posX<width&&posY>=0&&posY<height){
			matrix[posX][posY]=s.pixels[posX-x][posY-y];
			}
		}
	}
}
/**
 * @uml.property  name="v"
 */
int v = 0;
public void paintImage(Graphics g,JPanel panel){
	//TODO eine tabelle f�r h�he und eine f�r breite -> deutlich schnelleres rendern
	int frameHeight = panel.getHeight();
	int frameWidth = panel.getWidth();
	int[] posx = new int[width+1];
	int[] posy = new int[height+1];
	for(int x=0;x<width+1;x++){
		posx[x]=(int)(frameWidth*x/width);
	}
	for(int y=0;y<height+1;y++){
		
		posy[y]=(int)(frameHeight*y/height);
	}
	for(int x=0;x<width;x++){
		for(int y=0;y<height;y++){
			g.setColor(matrix[x][y]);
			g.fillRect(posx[x], posy[y], posx[x+1]-posx[x], posy[y+1]-posy[y]);
			//Mekre (int)(frameHeight*y/height)
		}
	}
}
}
