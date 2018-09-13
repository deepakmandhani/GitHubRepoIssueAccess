package sigtuple.com.sigtuple.models;

import io.realm.RealmObject;

public class PullRequest extends RealmObject
{
    private String url;

    public String getUrl() { return this.url; }

    public void setUrl(String url) { this.url = url; }

    private String html_url;

    public String getHtmlUrl() { return this.html_url; }

    public void setHtmlUrl(String html_url) { this.html_url = html_url; }

    private String diff_url;

    public String getDiffUrl() { return this.diff_url; }

    public void setDiffUrl(String diff_url) { this.diff_url = diff_url; }

    private String patch_url;

    public String getPatchUrl() { return this.patch_url; }

    public void setPatchUrl(String patch_url) { this.patch_url = patch_url; }
}
