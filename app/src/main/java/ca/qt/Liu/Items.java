package ca.qt.Liu;

/**
 * nested class for Books, get kind ,id , volumInfo
 */
public class Items {
    public String kind;
    public String id;
    public String etag;
    public VolumeInfo volumeInfo;

    @Override
    public String toString() {
        return id + " " + volumeInfo.title + " " + volumeInfo.authors;
    }
}