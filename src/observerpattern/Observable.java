package observerpattern;

/**
 * Created by Yixiu Liu on 9/3/2016.
 */
public interface Observable<Context> {
    void addObserver(Observer<Context> observer);
    void notifyObservers(Context observable);
}
