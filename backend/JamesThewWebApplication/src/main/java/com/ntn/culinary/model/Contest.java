package com.ntn.culinary.model;

import java.sql.Date;
import java.util.List;

public class Contest {
    private int id;
    private String articleBody;
    private String headline;
    private String description;
    private Date datePublished;
    private Date dateModified;
    private List<ContestImages> contestImages;
    private String accessRole;

    public Contest() {
    }

    public Contest(int id, String articleBody, String headline, String description, Date datePublished, Date dateModified, List<ContestImages> contestImages, String accessRole) {
        this.id = id;
        this.articleBody = articleBody;
        this.headline = headline;
        this.description = description;
        this.datePublished = datePublished;
        this.dateModified = dateModified;
        this.contestImages = contestImages;
        this.accessRole = accessRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public List<ContestImages> getContestImages() {
        return contestImages;
    }

    public void setContestImages(List<ContestImages> contestImages) {
        this.contestImages = contestImages;
    }

    public String getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }


    @Override
    public String toString() {
        return "Contest{" +
                "id=" + id +
                ", articleBody='" + articleBody + '\'' +
                ", headline='" + headline + '\'' +
                ", description='" + description + '\'' +
                ", datePublished=" + datePublished + '\'' +
                ", dateModified=" + dateModified + '\'' +
                ", contestImages=" + contestImages + '\'' +
                ", accessType='" + accessRole + '\'' +
                '}';
    }
}
