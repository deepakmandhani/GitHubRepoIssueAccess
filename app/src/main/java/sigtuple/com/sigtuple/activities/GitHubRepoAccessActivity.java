package sigtuple.com.sigtuple.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sigtuple.com.githubrepoaccess.R;
import sigtuple.com.sigtuple.SigTupleApplication;
import sigtuple.com.sigtuple.adapters.GitHubIssueListRecyclerViewAdapter;
import sigtuple.com.sigtuple.models.RootObject;
import sigtuple.com.sigtuple.mvp.presenters.GitHubRepoPresenter;
import sigtuple.com.sigtuple.mvp.views.IGitHubRepoView;

public class GitHubRepoAccessActivity extends AppCompatActivity implements IGitHubRepoView {

    @Inject
    GitHubRepoPresenter gitHubRepoPresenter;

    @Nullable
    @BindView(R.id.user_serach_et)
    EditText userEditText;

    @Nullable
    @BindView(R.id.repo_search_et)
    EditText repoEditText;

    @Nullable
    @BindView(R.id.github_issues_rv)
    RecyclerView gitHubRepoIssueRecyclerView;

    @Nullable
    @BindView(R.id.github_issues_pb)
    ProgressBar gitHubRepoIssueProgressBar;

    @BindView(R.id.github_issues_no_result)
    ImageView githubIssueNotFoundIcon;

    Unbinder unbinder;

    public GitHubIssueListRecyclerViewAdapter gitHubIssueListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actibity_github_repo_access);
        unbinder = ButterKnife.bind(this);
        initDependecies();
        initRecycler();
        gitHubRepoPresenter.setiGitHubRepoView(this);
    }

    private void initDependecies() {
        ((SigTupleApplication) getApplication()).getApplicationComponent().inject(this);
    }

    private void initRecycler() {
        gitHubIssueListRecyclerViewAdapter = new GitHubIssueListRecyclerViewAdapter(this, new ArrayList<RootObject>());
        gitHubRepoIssueRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gitHubRepoIssueRecyclerView.setAdapter(gitHubIssueListRecyclerViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void showLoading() {
        gitHubRepoIssueProgressBar.setVisibility(View.VISIBLE);
        gitHubRepoIssueRecyclerView.setVisibility(View.GONE);
        githubIssueNotFoundIcon.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        gitHubRepoIssueProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showGitHubIssueListing(List<RootObject> rootObjectList) {
        gitHubRepoIssueRecyclerView.setVisibility(View.VISIBLE);
        githubIssueNotFoundIcon.setVisibility(View.GONE);
        gitHubIssueListRecyclerViewAdapter.updateGitHubIssueList(rootObjectList);
    }

    @Override
    public void showErrorView() {
        gitHubRepoIssueProgressBar.setVisibility(View.GONE);
        gitHubRepoIssueRecyclerView.setVisibility(View.GONE);
        githubIssueNotFoundIcon.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.github_issues_search_ic)
    public void onSearchIconClicked() {
        String userName = userEditText.getText().toString();
        String repoName = repoEditText.getText().toString();

        if(userName != null && !userName.isEmpty() && repoName != null && !repoName.isEmpty()) {
            long currTime = System.currentTimeMillis();
            gitHubRepoPresenter.checkLastTimeAccessed(userName, repoName, currTime, isNetworkConeected());
        } else {
            Toast.makeText(this, "Please enter correct GitHib user and repository combination.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkConeected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
