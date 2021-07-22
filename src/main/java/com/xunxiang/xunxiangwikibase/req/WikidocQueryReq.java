package com.xunxiang.xunxiangwikibase.req;

public class WikidocQueryReq extends PageReq {

    private String name;

    private Boolean checked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "WikidocQueryReq{" +
                "name='" + name + '\'' +
                ", checked=" + checked +
                '}';
    }
}