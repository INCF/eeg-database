/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.elasticsearch.entities;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 *
 * @author bydga
 */
@Document(indexName = "eegportal", type = "experiment", replicas = 0, shards = 5, indexStoreType = "fs")
public class ExperimentElastic {

	@Id
	private String experimentId;
	@Field(type = FieldType.Nested)
	private List<GenericParameter> params = new ArrayList<GenericParameter>();
	@Field(type = FieldType.Integer)
	private int userId;
	@Field(type = FieldType.Integer)
	private int groupId;

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

	public List<GenericParameter> getParams() {
		return params;
	}

	public void setParams(List<GenericParameter> params) {
		this.params = params;
	}

	public String getExperimentId() {
		return this.experimentId;
	}

	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}
}
