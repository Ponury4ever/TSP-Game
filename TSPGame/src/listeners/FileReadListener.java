package listeners;

/**
 * Created by PC on 2018-04-05.
 */
public interface FileReadListener {

    void onSuccess();

    void onFailed(String message);
}
