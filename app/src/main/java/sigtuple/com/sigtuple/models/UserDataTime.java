package sigtuple.com.sigtuple.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserDataTime extends RealmObject {

    private String owner;

    @PrimaryKey
    private String repo;

    private long lastTimeDataFetched;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public long getLastTimeDataFetched() {
        return lastTimeDataFetched;
    }

    public void setLastTimeDataFetched(long lastTimeDataFetched) {
        this.lastTimeDataFetched = lastTimeDataFetched;
    }
}
