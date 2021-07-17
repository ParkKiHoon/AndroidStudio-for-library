package com.unity3d.player;

public class BikeData {

    private String name;
    private String part;
    private String image;
    public BikeData(String name, String part,String image) {
        this.name = name;
        this.part = part;
        this.image=image;

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

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

}
