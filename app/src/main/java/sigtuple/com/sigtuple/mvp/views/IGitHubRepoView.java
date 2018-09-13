package sigtuple.com.sigtuple.mvp.views;

import java.util.List;

import sigtuple.com.sigtuple.models.RootObject;

public interface IGitHubRepoView {

    void showLoading();
    void hideLoading();
    void showGitHubIssueListing(List<RootObject> rootObjectList);
    void showErrorView();
}
