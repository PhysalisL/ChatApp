package observerpattern;

/**
 * Created by Yixiu Liu on 9/4/2016.
 */
public interface ClientConnectionObservable {
    void addObserver(ClientConnectionObserver observer);
    void notifyObservers(String ip, String name);
}
