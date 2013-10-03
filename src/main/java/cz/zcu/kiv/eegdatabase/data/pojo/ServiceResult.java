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
 *   ServiceResult.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;


import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 9.11.11
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="SERVICE_RESULT")
public class ServiceResult implements Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RESULT_ID")
    private int serviceResultId;
    @Column(name = "FIGURE")
    private Blob figure;
    @SolrField(name = IndexField.NAME)
    @Column(name = "FILENAME")
    private String filename;
    @Column(name = "STATUS")
    private String status;
    @SolrField(name = IndexField.TITLE)
    @Column(name = "TITLE")
    private String title;
    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person owner;

    public int getServiceResultId() {
        return serviceResultId;
    }

    public void setServiceResultId(int serviceResultId) {
        this.serviceResultId = serviceResultId;
    }

    public Blob getFigure() {
        return figure;
    }

    public void setFigure(Blob figure) {
        this.figure = figure;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
