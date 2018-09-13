package sigtuple.com.sigtuple.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sigtuple.com.sigtuple.models.RootObject;

public interface GithubApiInterface {

    @GET("repos/{username}/{reponame}/issues?")
    Observable<List<RootObject>> getGitHubPublicRepoResult(@Path("username") String username, @Path("reponame") String reponame, @Query("state") String s);
}
