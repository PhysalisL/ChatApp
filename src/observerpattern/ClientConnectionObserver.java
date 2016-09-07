package observerpattern;

/**
 * Created by Yixiu Liu on 9/4/2016.
 */
public interface ClientConnectionObserver {
    void beNotified(String ip, String name);
}
