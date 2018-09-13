package sigtuple.com.sigtuple.models;

import io.realm.RealmObject;

public class Label extends RealmObject
{
    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private String node_id;

    public String getNodeId() { return this.node_id; }

    public void setNodeId(String node_id) { this.node_id = node_id; }

    private String url;

    public String getUrl() { return this.url; }

    public void setUrl(String url) { this.url = url; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String color;

    public String getColor() { return this.color; }

    public void setColor(String color) { this.color = color; }
}
