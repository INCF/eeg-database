/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Vlasimsky
 */
@Entity
//@Indexed//Mark for indexing
//@Analyzer(impl = StandardAnalyzer.class)
@javax.persistence.Table(name = "ARTICLES")
public class Article implements java.io.Serializable {

    //@DocumentId
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ARTICLE_ID")
    private int articleId;
    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
    private ResearchGroup researchGroup;
    /*
    @Fields({
            @Field(index = Index.TOKENIZED), // same property indexed multiple times
            @Field(store = Store.YES),       // title value is stored in the index
            @Field(name = "title")})   // use a different field name
    */
    @Column(name = "TITLE")
    @SolrField(name = IndexField.TITLE) // Solr fulltext search
    private String title;
    /*
    @Fields({
            @Field(index = Index.TOKENIZED), // same property indexed multiple times
            @Field(store = Store.YES),       // text value is stored in the index
            @Field(name = "text")})   // use a different field name
    */
    @SolrField(name = IndexField.TEXT)
    @Column(name = "TEXT")
    @Lob
    private String text;
    @Column(name = "TIME")
    private Timestamp time;
    private boolean userMemberOfGroup; // changes dynamically from app
    private boolean userIsOwnerOrAdmin; // changes dynamically from app
    @OneToMany(mappedBy = "article")
    private Set<ArticleComment> articleComments = new HashSet<ArticleComment>(0);
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Person> subscribers = new HashSet<Person>(0);

    public Article() {
    }

    public Set<Person> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Person> subscribers) {
        this.subscribers = subscribers;
    }


    public Set<ArticleComment> getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(Set<ArticleComment> articleComments) {
        this.articleComments = articleComments;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person personByOwnerId) {
        this.person = personByOwnerId;
    }

    public ResearchGroup getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUserMemberOfGroup() {
        return userMemberOfGroup;
    }

    public void setUserMemberOfGroup(boolean memberOfGroup) {
        this.userMemberOfGroup = memberOfGroup;
    }

    public boolean isUserIsOwnerOrAdmin() {
        return userIsOwnerOrAdmin;
    }

    public void setUserIsOwnerOrAdmin(boolean userIsOwnerOrAdmin) {
        this.userIsOwnerOrAdmin = userIsOwnerOrAdmin;
    }
}
