/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.zip;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author Jan Štěbeták
 */
public interface Generator {

  public OutputStream generate(Experiment exp, MetadataCommand mc, Set<DataFile> datas) throws Exception, SQLException, IOException;
}
