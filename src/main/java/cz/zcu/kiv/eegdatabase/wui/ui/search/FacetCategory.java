package cz.zcu.kiv.eegdatabase.wui.ui.search;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 8.4.13
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
public class FacetCategory implements Serializable {

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
}
