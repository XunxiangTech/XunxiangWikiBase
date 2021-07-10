package com.xunxiang.xunxiangwikibase.req;

import java.util.Date;

public class WikibookQueryReq extends PageReq {

    private String title;

    private Long category2Id;

    private String description;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategory2Id() {
        return category2Id;
    }

    public void setCategory2Id(Long category2Id) {
        this.category2Id = category2Id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "WikibookQueryReq{" +
                "title='" + title + '\'' +
                ", category2Id=" + category2Id +
                ", description='" + description + '\'' +
                '}';
    }
}