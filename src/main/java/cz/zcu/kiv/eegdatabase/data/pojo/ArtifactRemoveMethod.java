package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
public class ArtifactRemoveMethod implements Serializable {

    private int artifactRemoveMethodId;
    private String name;
    private String description;
    Set<Artifact> artifacts = new HashSet<Artifact>(0);

    public ArtifactRemoveMethod() {
    }

    public ArtifactRemoveMethod(int artifactRemoveMethodId, String name, String description, Set<Artifact> artifacts) {
        this.artifactRemoveMethodId = artifactRemoveMethodId;
        this.name = name;
        this.description = description;
        this.artifacts = artifacts;
    }

    public int getArtifactRemoveMethodId() {
        return artifactRemoveMethodId;
    }

    public void setArtifactRemoveMethodId(int artifactRemoveMethodId) {
        this.artifactRemoveMethodId = artifactRemoveMethodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
}
