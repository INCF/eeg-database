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
    private String title;
    private String description;
    Set<Experiment> experiments = new HashSet<Experiment>(0);
    private int defaultNumber;

    public ArtifactRemoveMethod() {
    }

    public ArtifactRemoveMethod(int artifactRemoveMethodId, String title, String description, Set<Experiment> experiments) {
        this.artifactRemoveMethodId = artifactRemoveMethodId;
        this.title = title;
        this.description = description;
        this.experiments = experiments;
    }

    public int getArtifactRemoveMethodId() {
        return artifactRemoveMethodId;
    }

    public void setArtifactRemoveMethodId(int artifactRemoveMethodId) {
        this.artifactRemoveMethodId = artifactRemoveMethodId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }
}
