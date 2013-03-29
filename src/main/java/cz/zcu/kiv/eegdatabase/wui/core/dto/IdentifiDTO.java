package cz.zcu.kiv.eegdatabase.wui.core.dto;

import java.io.Serializable;

/**
 * Object for identifiable DTOs. Its parent object. Implemented equals and hashcode.
 * 
 * @author Jakub Rinkes
 * 
 */
public class IdentifiDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IdentifiDTO other = (IdentifiDTO) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
