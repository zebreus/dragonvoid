package tbs;

public interface WorldFeedbackReceiver {
	public abstract void WorldLoaded();
	public abstract void WorldLoadingProgress(int percentage);
}
