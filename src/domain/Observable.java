package domain;


public interface Observable {

    public void setObserver(Observer observer);
    
    public void notifyOutput(String o);
    
    public String notifyInput();
    
}
