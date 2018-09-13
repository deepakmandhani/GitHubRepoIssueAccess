package sigtuple.com.sigtuple;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import sigtuple.com.sigtuple.db.GitHubDatabaseService;
import sigtuple.com.sigtuple.di.componenets.ApplicationComponent;
import sigtuple.com.sigtuple.di.componenets.DaggerApplicationComponent;
import sigtuple.com.sigtuple.di.modules.ApplicationModule;
import sigtuple.com.sigtuple.di.modules.NetworkModule;

public class SigTupleApplication extends Application {

    private static String DB_NAME = "GitHubIssue";
    private static long DB_SCHEMA = 1;
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule())
                .applicationModule(new ApplicationModule(this))
                .build();
        initRealm();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void initRealm() {
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(DB_SCHEMA)
                .name(DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(!Realm.getDefaultInstance().isClosed())
        GitHubDatabaseService.getInstance().closeRealm();
    }
}
