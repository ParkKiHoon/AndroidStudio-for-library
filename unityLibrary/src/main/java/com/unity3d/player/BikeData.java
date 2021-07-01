package com.unity3d.player;

public class BikeData {

    private String name;
    private String part;

    public BikeData(String name, String part) {
        this.name = name;
        this.part = part;

    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getpart() {
        return part;
    }

    public void setpart(String part) {
        this.part = part;
    }

}
