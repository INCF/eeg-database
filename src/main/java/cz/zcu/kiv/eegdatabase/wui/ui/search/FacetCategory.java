package cz.zcu.kiv.eegdatabase.wui.ui.search;

import java.io.Serializable;

/**
 * Represents a category of faceted search. Each category maintains its name and a number of documents in it.
 * User: Jan Koren
 * Date: 8.4.13
 * Time: 16:54
 */
public class FacetCategory implements Serializable, Comparable<FacetCategory> {

    private String name;
    private long count;

    private static final long serialVersionUID = 5458851218415844461L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public FacetCategory(String name, long count) {
        this.name = name;
        this.count = count;
    }

    /**
     * @see Comparable#compareTo(Object)
     * @param anotherCategory another facet category for comparison.
     * @return
     */
    @Override
    public int compareTo(FacetCategory anotherCategory) {

        int diff = - (int)(this.getCount() -  anotherCategory.getCount());
        if (diff == 0) {
            return this.getName().compareTo(anotherCategory.getName());
        }

        return diff;
    }
}
