package id.co.rumahcoding.qaamus.models;

import io.realm.RealmObject;

/**
 * Created by agungaprian on 28/05/17.
 */

public class Entry extends RealmObject {
    private long id;
    private String arab;
    private String indonesia;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArab() {
        return arab;
    }

    public void setArab(String arab) {
        this.arab = arab;
    }

    public String getIndonesia() {
        return indonesia;
    }

    public void setIndonesia(String indonesia) {
        this.indonesia = indonesia;
    }
}
