package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class Artifact implements Serializable {

    private int artifactId;
    private Set<ArtifactRemoveMethod> artifactRemoveMethods = new HashSet<ArtifactRemoveMethod>(0);

    public Artifact() {
    }
}


