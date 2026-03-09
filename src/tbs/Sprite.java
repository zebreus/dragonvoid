package tbs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite implements Drawable{
/**
 * @uml.property  name="pixels" multiplicity="(0 -1)" dimension="2"
 */
Color[][] pixels;
/**
 * @uml.property  name="alpha"
 */
int[][] alpha;
/**
 * @uml.property  name="width"
 */
int width;
/**
 * @uml.property  name="height"
 */
int height;

/**
 * 
 * @param name
 */
String name;

public Sprite(String path) throws IOException {
try{
InputStream is = Sprite.class.getClassLoader().getResourceAsStream("res/sprites/" + path + ".png");
if (is == null) throw new IOException("Resource not found: res/sprites/" + path + ".png");
try {
BufferedImage image = ImageIO.read(is);

name =  path.split("/")[path.split("/").length-1];
width = image.getWidth();
height = image.getHeight();
pixels = new Color[width][height];
alpha = new int[width][height];
for (int x = 0; x < width; x++) {
for (int y = 0; y < height; y++) {
pixels[x][y] = new Color(image.getRGB(x, y));
}
}
try{
WritableRaster alphaRaster = image.getAlphaRaster();
for(int x = 0;x<alphaRaster.getWidth();x++){
for(int y = 0;y<alphaRaster.getHeight();y++){
alpha[x][y] = alphaRaster.getSample(x, y, 0);
}
}
}catch(Exception e){
for(int x = 0;x<width;x++){
for(int y = 0;y<height;y++){
alpha[x][y] = 255;
}
}
}
} finally {
is.close();
}
}catch(Exception e){
System.err.println("Error loading res/sprites/"+path+".png");
e.printStackTrace();
}
}


/**
 * @return
 * @uml.property  name="width"
 */
public int getWidth() {
return width;
}

/**
 * @return
 * @uml.property  name="height"
 */
public int getHeight() {
return height;
}


/**
 * @return
 * @uml.property  name="alpha"
 */
public int[][] getAlpha() {
	return alpha;
}


public Color[][] getColors() {
	return pixels;
}


@Override
public void paint(Matrix mat, int x, int y) {
	mat.drawDrawable(this, x, y);
}


@Override
public String getName() {
	return name;
}
}