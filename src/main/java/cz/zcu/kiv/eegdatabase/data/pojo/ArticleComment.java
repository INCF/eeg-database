package cz.zcu.kiv.eegdatabase.data.pojo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Vlasimsky
 */
@Entity
@Indexed//Mark for indexing
@Analyzer(impl = StandardAnalyzer.class)
@javax.persistence.Table(name = "ARTICLES_COMMENTS")
public class ArticleComment {

    @DocumentId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMENT_ID")
    private int commentId;
    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private ArticleComment parent;
    @OneToMany(mappedBy = "parent")
    private Set<ArticleComment> children = new HashSet<ArticleComment>(0);
    @Fields({
            @Field(index = Index.TOKENIZED), // same property indexed multiple times
            @Field(store = Store.YES), // text value is stored in the index
            @Field(name = "text")})
    @Column(name = "TEXT")
    @Lob
    private String text;
    @Column(name = "TIME")
    private Timestamp time;
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;
    private boolean userMemberOfGroup; // changes dynamically from app
    private boolean userIsOwnerOrAdmin; // changes dynamically from app

    public ArticleComment() {
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public ArticleComment getParent() {
        return parent;
    }

    public void setParent(ArticleComment parent) {
        this.parent = parent;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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

    public boolean isUserIsOwnerOrAdmin() {
        return userIsOwnerOrAdmin;
    }

    public void setUserIsOwnerOrAdmin(boolean userIsOwnerOrAdmin) {
        this.userIsOwnerOrAdmin = userIsOwnerOrAdmin;
    }

    public boolean isUserMemberOfGroup() {
        return userMemberOfGroup;
    }

    public void setUserMemberOfGroup(boolean userMemberOfGroup) {
        this.userMemberOfGroup = userMemberOfGroup;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Set<ArticleComment> getChildren() {
        return children;
    }

    public void setChildren(Set<ArticleComment> children) {
        this.children = children;
    }
}
