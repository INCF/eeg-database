package cz.zcu.kiv.eegdatabase.data.pojo;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jiri Vlasimsky
 */
public class ArticleComment {

  private int commentId;
  private Person person;
  private ArticleComment parent;
  private Set<ArticleComment> children = new HashSet<ArticleComment>(0);

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
