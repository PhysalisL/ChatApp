package observerpattern;

/**
 * Created by Yixiu Liu on 9/3/2016.
 */
public interface Observer<Context> {
    void beNotified(Context observable);
}
