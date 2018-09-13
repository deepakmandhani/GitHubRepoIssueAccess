package sigtuple.com.sigtuple.mvp.presenters;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import sigtuple.com.sigtuple.db.GitHubDatabaseService;
import sigtuple.com.sigtuple.models.RootObject;
import sigtuple.com.sigtuple.models.UserDataTime;
import sigtuple.com.sigtuple.mvp.views.IGitHubRepoView;
import sigtuple.com.sigtuple.network.GithubApiInterface;

public class GitHubRepoPresenter {

    GithubApiInterface githubApiInterface;
    IGitHubRepoView iGitHubRepoView;

    @Inject
    GitHubRepoPresenter(GithubApiInterface githubApiInterface) {
        this.githubApiInterface = githubApiInterface;
    }

    public void setiGitHubRepoView(IGitHubRepoView iGitHubRepoView) {
        this.iGitHubRepoView = iGitHubRepoView;
    }

    public void checkLastTimeAccessed(final String user, final String repo, final long curreTime, final boolean isNetworkAvailable) {
        iGitHubRepoView.showLoading();
        Observable.just(GitHubDatabaseService.getInstance().checkUserTimeEntry(user, repo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserDataTime>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<UserDataTime> value) {
                        for (UserDataTime userDataTime: value
                             ) {
                            if (curreTime - userDataTime.getLastTimeDataFetched() < 1800000L) {
                                getSearchGitHubIssueFromLocal(user, repo);
                                return;
                            }
                        }
                            if(isNetworkAvailable) {
                                getGitHubRepoIssues(user, repo);
                                addUserTimeEntryToDB(user, repo, curreTime);
                                return;
                            }
                            iGitHubRepoView.showErrorView();
                            iGitHubRepoView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void addUserTimeEntryToDB(final String user, final String repo, final long curreTime) {
        GitHubDatabaseService.getInstance().addUserTimeEntry(user, repo, curreTime);
    }

    public void getGitHubRepoIssues(final String user, final String repo) {

        iGitHubRepoView.showLoading();
        Observable<List<RootObject>> openListObservable = getObservableOfList(user, repo, "open");
        Observable<List<RootObject>> closeListObservable = getObservableOfList(user, repo, "close");

        openListObservable.mergeWith(closeListObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RootObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<RootObject> rootObjectListResponse) {
                        if (rootObjectListResponse != null && !rootObjectListResponse.isEmpty()) {
                            iGitHubRepoView.showGitHubIssueListing(rootObjectListResponse);
                            for (RootObject rootObject: rootObjectListResponse
                                 ) {
                                rootObject.setOwner(user);
                                rootObject.setRepo(repo);
                            }
                            saveGitHubIssueToLocal(rootObjectListResponse);
                            return;
                        }
                        iGitHubRepoView.showErrorView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iGitHubRepoView.showErrorView();
                    }

                    @Override
                    public void onComplete() {
                        iGitHubRepoView.hideLoading();
                    }
                });
    }

    private Observable<List<RootObject>> getObservableOfList(final String user, final String repo, String query) {
        return  githubApiInterface.getGitHubPublicRepoResult(user , repo ,query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void saveGitHubIssueToLocal(List<RootObject> rootObjectList) {
        GitHubDatabaseService.getInstance().addWikiPagesToLocal(rootObjectList);
    }

    public void getSearchGitHubIssueFromLocal(String user, String repo) {
        iGitHubRepoView.showLoading();
        Observable.just(GitHubDatabaseService.getInstance().searchWikiPageFromLocal(user, repo))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RootObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<RootObject> value) {
                        if(value != null && !value.isEmpty())
                            iGitHubRepoView.showGitHubIssueListing(value);
                        else
                            iGitHubRepoView.showErrorView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iGitHubRepoView.showErrorView();
                    }

                    @Override
                    public void onComplete() {
                        iGitHubRepoView.hideLoading();
                    }
                });
    }

}