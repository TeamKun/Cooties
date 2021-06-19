package net.kunmc.lab.cooties.cooties;

public class CootiesState {
    String name;
    int time;
    boolean isInit;

    public CootiesState(String name, int time) {
        this.name = name;
        this.time = time;
        isInit = true;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int time){
        this.time = time;
    }

    public void setIsInit(boolean isInit) {
        this.isInit = isInit;
    }

    public boolean getIsInit() {
        return isInit;
    }
}
