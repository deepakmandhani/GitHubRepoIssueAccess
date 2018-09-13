package sigtuple.com.sigtuple.db;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import sigtuple.com.sigtuple.models.RootObject;
import sigtuple.com.sigtuple.models.UserDataTime;

public class GitHubDatabaseService {

    private static GitHubDatabaseService databaseService;
    private final Realm realm;

    private GitHubDatabaseService() {
        realm = Realm.getDefaultInstance();
    }

    public static GitHubDatabaseService getInstance() {
        if (databaseService == null) {
            databaseService = new GitHubDatabaseService();
        }
        return databaseService;
    }

    public void addWikiPagesToLocal(List<RootObject> pageList) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(pageList);
        realm.commitTransaction();
    }

    public List<RootObject> searchWikiPageFromLocal(String user, String repo) {
        List<RootObject> pageList = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<RootObject> pageRealmResults = realm.where(RootObject.class).equalTo("owner", user).equalTo("repo", repo).findAll();
        for(RootObject page : realm.copyFromRealm(pageRealmResults)) {
                pageList.add(page);
        }
        realm.commitTransaction();
        return pageList;
    }

    public void closeRealm() {
        realm.close();
    }

    public void addUserTimeEntry(String user, String repo, long time) {
        UserDataTime userDataTime = new UserDataTime();
        userDataTime.setOwner(user);
        userDataTime.setRepo(repo);
        userDataTime.setLastTimeDataFetched(time);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userDataTime);
        realm.commitTransaction();

    }

    public List<UserDataTime> checkUserTimeEntry(String user, String repo) {
        List<UserDataTime> userDataTimes = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<UserDataTime> userDataTimeRealmResults = realm.where(UserDataTime.class)
                .equalTo("owner", user).equalTo("repo", repo)
                .findAll();
        for(UserDataTime userDataTime : realm.copyFromRealm(userDataTimeRealmResults)) {
            userDataTimes.add(userDataTime);
        }
        realm.commitTransaction();
        return userDataTimes;
    }
}
