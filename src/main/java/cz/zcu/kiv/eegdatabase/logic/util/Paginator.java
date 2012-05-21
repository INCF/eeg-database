package cz.zcu.kiv.eegdatabase.logic.util;

/**
 * Created with IntelliJ IDEA.
 * User: Jindra
 * Date: 27.4.12
 * Time: 15:54
 */
public class Paginator {
    private int itemCount;
    private int itemsPerPage = 10;
    private int pageCount;
    private int actualPage = 1;
    private String baseUrl = "?page=%1$d";

    public Paginator(int itemCount, int itemsPerPage) {
        this.itemCount = itemCount;
        this.itemsPerPage = itemsPerPage;
        this.pageCount = (int) Math.ceil(itemCount / (double) itemsPerPage);
    }

    public Paginator(int itemCount, int itemsPerPage, String baseUrl) {
        this(itemCount, itemsPerPage);
        this.baseUrl = baseUrl;
    }

    private String getLink(int page, String title, boolean disabled) {
        if (disabled) {
            return " <span class=\"disabled\">" + title + "</span> ";
        }
        return " <a href=\"" + String.format(baseUrl, page) + "\">" + title + "</a> ";
    }

    public int getActualPage() {
        return actualPage;
    }

    public void setActualPage(int actualPage) {
        if (actualPage < 1) {
            this.actualPage = 1;
        } else if (actualPage > pageCount) {
            this.actualPage = pageCount;
        } else {
            this.actualPage = actualPage;
        }
    }

    public String getLinks() {
        String result = "<div class=\"paginator\">";
        result += getLink(1, "&lt;&lt;", actualPage == 1);
        result += getLink(actualPage - 1, "&lt;", actualPage == 1);
        result += "<span class=\"description\">Page " + actualPage + " of " + pageCount + "</span>";
        result += getLink(actualPage + 1, "&gt;", actualPage == pageCount);
        result += getLink(pageCount, "&gt;&gt;", actualPage == pageCount);
        result += "</div>";
        return result;
    }

    public int getFirstItemIndex() {
        return ((actualPage - 1) * itemsPerPage);
    }

    public int getLastItemIndex() {
        return (actualPage * itemsPerPage) - 1;
    }
}
