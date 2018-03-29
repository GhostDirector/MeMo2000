package fi.tamk.tiko.memo2000;

/**
 * Created by ghost on 29.3.2018.
 */

public class Note {

    private String title;
    private String picPath;

    public Note(String title, String picPath){
        this.title = title;
        this.picPath = picPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
