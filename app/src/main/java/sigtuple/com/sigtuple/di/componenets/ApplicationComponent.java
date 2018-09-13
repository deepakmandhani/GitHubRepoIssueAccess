package sigtuple.com.sigtuple.di.componenets;

import javax.inject.Singleton;

import dagger.Component;
import sigtuple.com.sigtuple.activities.GitHubRepoAccessActivity;
import sigtuple.com.sigtuple.di.modules.ApplicationModule;
import sigtuple.com.sigtuple.di.modules.NetworkModule;

@Singleton
@Component(modules = { ApplicationModule.class, NetworkModule.class })
public interface ApplicationComponent {

    void inject(GitHubRepoAccessActivity gitHubRepoAccessActivity);
}
