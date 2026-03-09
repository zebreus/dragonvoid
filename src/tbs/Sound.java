package tbs;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	/**
	 * @uml.property  name="next"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	AudioInputStream next;
	
	/**
	 * constructs a new Sound from a .wav file. 
	 * @param path   res/sounds/ "here goes your input" .wav
	 * @uml.property  name="clips"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="javax.sound.sampled.Clip"
	 */
	ConcurrentLinkedQueue<Clip> clips;
	/**
	 * @uml.property  name="path"
	 */
	String path;
	public Sound(String path){
		clips = new ConcurrentLinkedQueue<Clip>();
		this.path = path;
		try{
		URL url = Sound.class.getClassLoader().getResource("res/sounds/"+path+".wav");
		if (url == null) throw new IOException("Resource not found: res/sounds/"+path+".wav");
		this.next = AudioSystem.getAudioInputStream(url);
		}catch(Exception e){
			System.err.println("Error loading audio file res/sounds/"+path+".wav");
		}
		}
	public void play(){
		final AudioInputStream play = next;
		Thread t = new Thread(){
			 @Override
			 public void run()
			 {
		AudioListener listener = new AudioListener();
		  try {
		    Clip clip = AudioSystem.getClip();
		    clip.addLineListener(listener);
		    clip.open(play);
		    try {
		      clips.add(clip);
		      clip.start();
		      listener.waitUntilDone();
		      System.out.println(clips.remove(clip));
		    } finally {
		      clip.close();
		    }
		  }catch(Exception e){
			  System.err.println("Error playing file");
			  e.printStackTrace();
		  }finally {
		    try {
				play.close();
			} catch (IOException e) {
				System.err.println("Error closing stream");
				e.printStackTrace();
			}
		  }
			 }
		};
		t.start();
		Thread d = new Thread(){
			 @Override
			 public void run()
			 {
				 try {
					URL url = Sound.class.getClassLoader().getResource("res/sounds/"+path+".wav");
					if (url != null) {
						next = AudioSystem.getAudioInputStream(url);
					}
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		};
		d.start();
	}
	public void stopAll(){
		while(!clips.isEmpty()){
			clips.poll().stop();
		}
	}
	public void stopFirst(){
		clips.poll().stop();
	}
	public Clip getPlaying(){
		return clips.peek();
	}
	private class AudioListener implements LineListener {
		    private boolean done = false;
		    @Override public synchronized void update(LineEvent event) {
		    Type eventType = event.getType();
		    if (eventType == Type.OPEN) {
		    }else
		    if (eventType == Type.START) {
			}else
			if (eventType == Type.STOP) {
				System.out.println("stop event");
				done = true;
			}else
			if (eventType == Type.CLOSE) {
				done = true;
			}
		    }
		    public synchronized void waitUntilDone() throws InterruptedException {
		      while (!done) {
		    	  wait(); }
		    }
		  }
}
