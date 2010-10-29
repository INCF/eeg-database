package cz.zcu.kiv.eegdatabase.data.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.annotations.Entity;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author Jiri Vlasimsky
 */
@Entity
@Indexed//Mark for indexing
@Analyzer(impl = StandardAnalyzer.class)
public class ArticleComment {

  @DocumentId
  private int commentId;
  private Person person;
  private ArticleComment parent;
  private Set<ArticleComment> children = new HashSet<ArticleComment>(0);
  @Fields({
    @Field(index = Index.TOKENIZED), // same property indexed multiple times
    @Field(store = Store.YES), // text value is stored in the index
    @Field(name = "text")})
  private String text;
  private Timestamp time;
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
