package tbs;

public interface SaveFeedbackReceiver {
public abstract void saveLoadingProgress(int percentage);
public abstract void saveLoaded();
public abstract void savingProgress(int percentage);
public abstract void saved();
public abstract void creatingError();
public abstract void creatingProgress(int percentage);
public abstract void created();
}
