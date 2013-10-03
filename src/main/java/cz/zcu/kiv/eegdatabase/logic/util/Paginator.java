/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   Paginator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
