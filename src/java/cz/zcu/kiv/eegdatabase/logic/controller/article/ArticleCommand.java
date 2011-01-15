
package cz.zcu.kiv.eegdatabase.logic.controller.article;

/**
 *
 * @author Jiri Vlasimsky
 */
public class ArticleCommand {
  private int articleId;
  private int personId;
  private int researchGroup;
  private String title;
  private String text;
  private String time;


  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  public int getPersonId() {
    return personId;
  }

  public void setPersonId(int personId) {
    this.personId = personId;
  }

  public int getResearchGroup() {
    return researchGroup;
  }

  public void setResearchGroup(int researchGroup) {
    this.researchGroup = researchGroup;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


}
