package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 *
 * @author Jindra
 */
public class AddDefectToPersonCommand {

    private int subjectId;
    private int defectId;

    public int getDefectId() {
        return defectId;
    }

    public void setDefectId(int defectId) {
        this.defectId = defectId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
