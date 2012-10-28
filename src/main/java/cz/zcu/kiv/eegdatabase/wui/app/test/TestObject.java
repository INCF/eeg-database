package cz.zcu.kiv.eegdatabase.wui.app.test;

import java.io.Serializable;

public class TestObject implements Serializable {
    
    private String name;
    
    public TestObject(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
