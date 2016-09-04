/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * ExperimentElastic.java, 2013/10/02 00:01 Martin Bydzovsky
 * ****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.nosql.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import odml.core.Section;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 *
 * @author bydga
 */
@Document(indexName = "eegdatabase", type = "experiment", replicas = 0, shards = 5)
public class ExperimentElastic implements Serializable {

	@Id
	private String experimentId;
	@Field(type = FieldType.Integer)
	private int userId;
	@Field(type = FieldType.Integer)
	private int groupId;
	
	@Field(type = FieldType.Object)
	private Section metadata;
	
	public ExperimentElastic() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}


	public String getExperimentId() {
		return this.experimentId;
	}

	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}
	
	public Section getMetadata() {
        return metadata;
    }
	
    public void setMetadata(Section metadata) {
        this.metadata = metadata;
    }
}
