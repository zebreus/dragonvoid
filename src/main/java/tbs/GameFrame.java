package tbs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame{
static GameFrame gameframe;
public static GameFrame getGameframe() {
	return gameframe;
}

/**
 * @param hook
 * @uml.property  name="hook"
 */
public void setHook(FrameHook hook) {
	this.hook = hook;
}
/**
 * @uml.property  name="matrix"
 * @uml.associationEnd  
 */
Matrix matrix;
/**
 * @uml.property  name="hook"
 * @uml.associationEnd  
 */
FrameHook hook;
/**
 * @uml.property  name="running"
 */
boolean running;
/**
 * @uml.property  name="framelock"
 */
int framelock;
/**
 * @uml.property  name="updatelock"
 */
int updatelock;
/**
 * @uml.property  name="panel"
 * @uml.associationEnd  inverse="this$0:tbs.GameFrame$GamePanel"
 */
GamePanel panel;
/**
 * @uml.property  name="buffer"
 */
BufferedImage buffer;
/**
 * @uml.property  name="gfxConf"
 */
private final GraphicsConfiguration gfxConf = GraphicsEnvironment
.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
public GameFrame(FrameHook hook, Matrix matrix,int framelock,int updatelock){
	super();
	this.hook = hook;
	this.matrix = matrix;
	this.framelock =framelock;
	this.updatelock = updatelock;
	running = false;
	gameframe = this;
}

public void start(){
	running = true;
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	panel = new GamePanel();
	FrameMouseListener mouse = new FrameMouseListener();
	panel.addMouseListener(mouse);
	panel.addMouseMotionListener(mouse);
	this.addKeyListener(new FrameKeyListener());
	panel.setPreferredSize(new Dimension(208,117));
	panel.requestFocus();
	panel.setMinimumSize(new Dimension(208,117));
	this.add(panel);
	this.pack();
	
	this.setVisible(true);
	
	long lastRender = System.currentTimeMillis();
	long lastUpdate = System.currentTimeMillis();
	while(running){
		boolean sleep = true;
		if(System.currentTimeMillis()-lastUpdate>(1000/updatelock)){
			sleep = false;
			update(System.currentTimeMillis()-lastUpdate);
			frameUpdate();
			lastUpdate = System.currentTimeMillis();
		}
		if(System.currentTimeMillis()-lastRender>(1000/framelock)){
			sleep = false;
			hook.repaint(matrix);
			repaint();
			lastRender = System.currentTimeMillis();
		}
		if(sleep){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
public void frameUpdate(){
	DebugLayer.elements.put("Hooked Frame", hook.getClass().getSimpleName());
}
public void exit(){
}
public void update(long delta){
	hook.update(delta);
}
public static GameFrame getFrame(){
	return gameframe;
}
private class FrameKeyListener implements KeyListener{

	@Override
	public void keyPressed(KeyEvent arg0) {
		hook.keyPressed(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		hook.keyReleased(arg0.getKeyCode());
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		hook.keyTyped(arg0.getKeyCode());
		
	}
	
}
private class FrameMouseListener implements MouseListener, MouseMotionListener{

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int[] pos = positionConverter(arg0.getX(), arg0.getY());
		if(arg0.getButton()==MouseEvent.BUTTON1){
			hook.mouseLeftClick(pos[0],pos[1]);
		}
		if(arg0.getButton()==MouseEvent.BUTTON3){
			hook.mouseRightClick(pos[0],pos[1]);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		int[] pos = positionConverter(arg0.getX(), arg0.getY());
		if(arg0.getButton()==MouseEvent.BUTTON1){
			hook.mouseLeftPress(pos[0],pos[1]);
		}
		if(arg0.getButton()==MouseEvent.BUTTON3){
			hook.mouseRightPress(pos[0],pos[1]);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		int[] pos = positionConverter(arg0.getX(), arg0.getY());
		if(arg0.getButton()==MouseEvent.BUTTON1){
			hook.mouseLeftRelease(pos[0],pos[1]);
		}
		if(arg0.getButton()==MouseEvent.BUTTON3){
			hook.mouseRightRelease(pos[0],pos[1]);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int[] pos = positionConverter(arg0.getX(), arg0.getY());
		hook.mouseMove(pos[0],pos[1]);
		
	}
	public int[] positionConverter(int x,int y){
		Dimension dim = panel.getSize();
		int matriX = (208*x/dim.width);
		int matriY = (117*y/dim.height);
		int[] retarr = {matriX,matriY};
		return retarr;
		
	}
}
private class GamePanel extends JPanel{
	public GamePanel(){
		
	}
@Override
public void paint(Graphics g){
	buffer = gfxConf.createCompatibleImage( getWidth(), getHeight() );
	Graphics gr = buffer.createGraphics();
	matrix.paintImage(gr,this);
	DebugLayer.draw(gr);
	g.drawImage(buffer,0,0,this);
}
}
}
