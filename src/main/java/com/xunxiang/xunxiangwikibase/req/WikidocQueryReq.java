package com.xunxiang.xunxiangwikibase.req;

public class WikidocQueryReq extends PageReq {

    private String name;

    private Boolean checked;

    private Long wikibookId;

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

    public Long getWikibookId() {
        return wikibookId;
    }

    public void setWikibookId(Long wikibookId) {
        this.wikibookId = wikibookId;
    }

    @Override
    public String toString() {
        return "WikidocQueryReq{" +
                "name='" + name + '\'' +
                ", checked=" + checked +
                ", wikibookId=" + wikibookId +
                '}';
    }
}