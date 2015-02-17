/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   LicenseServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageLicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.LicenseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;

/**
 * 
 * @author J. Danek
 */
public class LicenseServiceImpl extends GenericServiceImpl<License, Integer> implements LicenseService {

    protected Log log = LogFactory.getLog(getClass());

    private ExperimentPackageLicenseDao experimentPackageLicenseDao;
    private LicenseDao licenseDao;
    private String publicLicenseFileName = "PublicLicense.pdf";

    public LicenseServiceImpl() {
    }

    @Required
    public void setExperimentPackageLicenseDao(ExperimentPackageLicenseDao experimentPackageLicenseDao) {
        this.experimentPackageLicenseDao = experimentPackageLicenseDao;
    }

    @Required
    public void setLicenseDao(LicenseDao licenseDao) {
        this.licenseDao = licenseDao;
    }

    public LicenseServiceImpl(GenericDao<License, Integer> dao) {
        super(dao);
    }

    public void setPublicLicenseFileName(String publicLicenseFileName) {
        this.publicLicenseFileName = publicLicenseFileName;
    }

    @Override
    @Transactional
    public Integer create(License newInstance) {

        try {

            InputStream fileContentStream = newInstance.getFileContentStream();
            if (fileContentStream != null) {
                Blob createBlob = licenseDao.getSessionFactory().getCurrentSession().getLobHelper().createBlob(fileContentStream, fileContentStream.available());
                newInstance.setAttachmentContent(createBlob);
            }

            return licenseDao.create(newInstance);

        } catch (HibernateException e) {
            log.error(e.getMessage(), e);
            return null;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    @Override
    @Transactional
    public void addLicenseForPackage(License license, ExperimentPackage pack) {

        this.checkLicenseGroupValidity(license, pack.getResearchGroup());

        License tmp;
        if (license.isTemplate()) {
            tmp = new License();
            tmp.copyFromTemplate(license);
        } else {
            tmp = license;
        }

        if (tmp.getLicenseId() == 0) {
            tmp.setResearchGroup(pack.getResearchGroup());
            int res = this.licenseDao.create(tmp);
            tmp.setLicenseId(res);
        }
        ExperimentPackageLicense conn = new ExperimentPackageLicense();
        conn.setExperimentPackage(pack);
        conn.setLicense(tmp);
        this.experimentPackageLicenseDao.create(conn);
    }

    @Override
    @Transactional
    public void removeLicenseFromPackage(License license, ExperimentPackage pack) {
        this.experimentPackageLicenseDao.removeLicenseFromPackage(pack.getExperimentPackageId(), license.getLicenseId());
    }

    @Override
    @Transactional(readOnly = true)
    public License getPublicLicense() {
        return this.licenseDao.getPublicLicense();
    }

    @Override
    @Transactional(readOnly = true)
    public List<License> getLicensesForGroup(ResearchGroup group, LicenseType type) {
        return this.licenseDao.getLicensesByType(group.getResearchGroupId(), type);
    }

    @Override
    @Transactional(readOnly = true)
    public License getOwnerLicense(ResearchGroup group) {
        return this.getLicensesForGroup(group, LicenseType.OWNER).get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<License> getLicensesForGroup(ResearchGroup group, List<LicenseType> type) {
        return this.licenseDao.getLicensesByType(group.getResearchGroupId(), type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<License> getLicensesForPackage(ExperimentPackage pckg) {
        List<ExperimentPackageLicense> connections = experimentPackageLicenseDao.readByParameter("experimentPackage.experimentPackageId", pckg.getExperimentPackageId());

        List<License> ret = new ArrayList<License>(connections.size());
        for (ExperimentPackageLicense epl : connections) {
            ret.add(epl.getLicense());
        }

        return ret;
    }

    private void checkLicenseGroupValidity(License license, ResearchGroup group)
    {
        if (!group.isPaidAccount() && license.getLicenseType() == LicenseType.BUSINESS) {
            throw new InvalidLicenseForPackageException("Group " + group.getTitle() + " is not a paid account and can not create bussiness licenses");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<License> getLicenseTemplates(ResearchGroup group) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("template", true);
        params.put("researchGroup.researchGroupId", group.getResearchGroupId());
        return this.licenseDao.readByParameter(params);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getLicenseAttachmentContent(int licenseId) {
        return licenseDao.getLicenseAttachmentContent(licenseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<License> getLicenseForPackageAndOwnedByPerson(int personId, int packageId) {
        return licenseDao.getLicenseForPackageAndOwnedByPerson(personId, packageId);
    }

    @Override
    @Transactional
    public void update(License transientObject) {
        try {
            // XXX WORKAROUND for Hibernate pre 4.0, update entity with blob this way.
            License merged = licenseDao.merge(transientObject);
            InputStream fileContentStream = transientObject.getFileContentStream();
            if (fileContentStream != null) {
                Blob createBlob;
                createBlob = licenseDao.getSessionFactory().getCurrentSession().getLobHelper().createBlob(fileContentStream, fileContentStream.available());
                merged.setAttachmentContent(createBlob);
                licenseDao.update(merged);
            }
        } catch (HibernateException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getPublicLicenseFile() {

        byte[] data = new byte[0];
        try {
            File file = ResourceUtils.getFile(publicLicenseFileName);
            if (file.exists()) {
                data = IOUtils.toByteArray(new FileInputStream(file));
            }
            return data;
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return data;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return data;
        }
    }
    
    @Override
    public String getPublicLicenseFileName() {
        return publicLicenseFileName;
    }

    @Override
    @Transactional(readOnly = true)
    public License getLicenseForPurchasedExperiment(int experimentId, int personId) {
        return licenseDao.getLicenseForPurchasedExperiment(experimentId, personId);
    }

    @Override
    @Transactional(readOnly = true)
    public License getLicenseForPurchasedExpPackage(int experimentPackageId, int personId) {
        return licenseDao.getLicenseForPurchasedExpPackage(experimentPackageId, personId);
    }

}
