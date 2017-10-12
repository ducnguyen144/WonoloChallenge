package wonolo.sgeoi.Interface;


public interface IOAuthDialogListener {
    public abstract void onComplete(String accessToken);
    public abstract void onError(String error);
}
