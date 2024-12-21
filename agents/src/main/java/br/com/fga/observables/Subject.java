package br.com.fga.observables;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObservable(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyAllObservers() {
        observers.forEach(Observer::update);
    }

}
