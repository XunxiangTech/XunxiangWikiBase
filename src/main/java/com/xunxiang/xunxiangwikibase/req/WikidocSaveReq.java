package com.xunxiang.xunxiangwikibase.req;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class WikidocSaveReq {
    private Long id;

    @NotNull(message = "【所属电子书】不能为空")
    private Long wikibookId;

    @NotNull(message = "【父文档】不能为空")
    private Long parent;

    @NotNull(message = "【名称】不能为空")
    private String name;

    private Integer viewCount;

    private Integer voteCount;

    private Date createTime;

    private Date updateTime;

    private Boolean checked;

    @NotNull(message = "【内容】不能为空")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWikibookId() {
        return wikibookId;
    }

    public void setWikibookId(Long wikibookId) {
        this.wikibookId = wikibookId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WikidocSaveReq{" +
                "id=" + id +
                ", wikibookId=" + wikibookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", checked=" + checked +
                ", content='" + content + '\'' +
                '}';
    }
}