package sigtuple.com.sigtuple.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sigtuple.com.githubrepoaccess.R;
import sigtuple.com.sigtuple.activities.WebViewActivity;
import sigtuple.com.sigtuple.models.PullRequest;
import sigtuple.com.sigtuple.models.RootObject;

public class GitHubIssueListRecyclerViewAdapter extends RecyclerView.Adapter<GitHubIssueListRecyclerViewAdapter.GitHubIssueItemViewHolder> {

    public Context context;
    public List<RootObject> rootObjectList;

    public GitHubIssueListRecyclerViewAdapter(Context context, List<RootObject> rootObjectList) {
        this.context = context;
        this.rootObjectList = rootObjectList;
    }

    @Override
    public GitHubIssueItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wiki_list_item, parent, false);
        return new GitHubIssueItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GitHubIssueItemViewHolder holder, final int position) {
        final RootObject value = rootObjectList.get(position);
        holder.issueId.setText(String.valueOf(value.getId()));
        holder.issueTitle.setText(value.getTitle());
        PullRequest pullRequest = value.getPullRequest();
        if(pullRequest != null && pullRequest.getPatchUrl() != null) {
            holder.issuePRLink.setText(Html.fromHtml("<U>" + pullRequest.getPatchUrl() + "</U>"));
            holder.issuePRLink.setTag(pullRequest.getPatchUrl());
        }
        holder.issuePRLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(WebViewActivity.GITHUB_PR_LINK, (String) view.getTag());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rootObjectList != null ? rootObjectList.size() : 0;
    }

    public void updateGitHubIssueList(List<RootObject> list) {
        if (!rootObjectList.isEmpty()) {
            rootObjectList.clear();
            rootObjectList.addAll(list);
        } else {
            rootObjectList = list;
        }
        notifyDataSetChanged();
    }

    public static class GitHubIssueItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.issue_id)
        TextView issueId;

        @BindView(R.id.issue_title)
        TextView issueTitle;

        @BindView(R.id.issue_pull_request_link)
        TextView issuePRLink;

        public GitHubIssueItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
