package net.kunmc.lab.cooties.cooties;

public class CootiesState {
    String name;
    int time;

    public CootiesState(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getTime(){
        return time;
    }

    public void setTime(int time){
        this.time = time;
    }
}
