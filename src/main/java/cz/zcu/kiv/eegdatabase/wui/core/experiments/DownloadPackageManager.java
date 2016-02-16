package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stebjan on 15.2.2016.
 */
public class DownloadPackageManager extends Thread implements Serializable{

    private int numberOfExperiments;
    private int numberOfDownloadedExperiments;
    private FileDTO outputFile;
    private ExperimentPackage expPackage;
    private MetadataCommand command;
    private License license;
    private List<Experiment> selectList;
    private ExperimentDownloadProvider downloadProvider;
    private Person loggedUser;

    public DownloadPackageManager(ExperimentPackage pckg, MetadataCommand mc, License license, List<Experiment> selectList,
                                  ExperimentDownloadProvider provider, Person user) {
        numberOfDownloadedExperiments = 0;
        numberOfExperiments = 0;
        expPackage = pckg;
        command = mc;
        this.license = license;
        this.selectList = selectList;
        downloadProvider = provider;
        loggedUser = user;

    }


    @Override
    public void run() {
        numberOfExperiments = selectList.size();
        outputFile = downloadProvider.generatePackageFile(expPackage, command, license, selectList, loggedUser, this);
    }

    public int getNumberOfExperiments() {
        return numberOfExperiments;
    }

    public void setNumberOfExperiments(int numberOfExperiments) {
        this.numberOfExperiments = numberOfExperiments;
    }

    public int getNumberOfDownloadedExperiments() {
        return numberOfDownloadedExperiments;
    }

    public void setNumberOfDownloadedExperiments(int numberOfDownloadedExperiments) {
        this.numberOfDownloadedExperiments = numberOfDownloadedExperiments;
    }

    public FileDTO getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(FileDTO outputFile) {
        this.outputFile = outputFile;
    }
}
