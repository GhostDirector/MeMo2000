package fi.tamk.tiko.memo2000;

/**
 * Note.
 */
public class Note {

    /**
     * The Title.
     */
    private String title;
    /**
     * The Pic path.
     */
    private String picPath;

    /**
     * Instantiates a new Note.
     *
     * @param title   the title
     * @param picPath the pic path
     */
    public Note(String title, String picPath){
        this.title = title;
        this.picPath = picPath;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets pic path.
     *
     * @return the pic path
     */
    public String getPicPath() {
        return picPath;
    }

    /**
     * Sets pic path.
     *
     * @param picPath the pic path
     */
    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
