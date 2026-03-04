package tbs;

import java.io.IOException;

public class StartArenaMode implements SaveFeedbackReceiver{
	public static void main(String[] args) {
		
		try {
			SaveManager saves = new SaveManager("Arena",new StartArenaMode());
			saves.loadSave();
			final WorldLoadingScreen wls = new WorldLoadingScreen("arena/world", saves);
			Thread t = new Thread() {
				@Override
				public void run() {
					new GameFrame(wls,new Matrix(208,117),60,1000).start();
				}
			};
			t.start();
			Thread.sleep(10);
			wls.loadWorld();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveLoadingProgress(int percentage) {
	}

	@Override
	public void saveLoaded() {
	}

	@Override
	public void savingProgress(int percentage) {
	}

	@Override
	public void saved() {
	}

	@Override
	public void creatingError() {
	}

	@Override
	public void creatingProgress(int percentage) {
	}

	@Override
	public void created() {
	}
}