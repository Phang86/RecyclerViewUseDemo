package com.gy.recyclerviewusedemo.bean;

public class ImageBean {
    private Object img;

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "img=" + img +
                '}';
    }

    public ImageBean(Object imgs) {
        this.img = imgs;
    }
}
