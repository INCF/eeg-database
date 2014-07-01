package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import java.io.Serializable;
import java.util.List;

/***********************************************************************************************************************
 *
 * This file is part of the ${PROJECT_NAME} project

 * ==========================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * ${NAME}, 2014/07/01 11:57 Prokop
 *
 **********************************************************************************************************************/
public class RowData implements Serializable{

    private String name;
    private Boolean required;
    private int maxCount;
    private List<RowData> subsections;

    public RowData(String name, Boolean required, int maxCount, List<RowData> subsections) {
        this.name = name;
        this.required = required;
        this.maxCount = maxCount;
        this.subsections = subsections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public List<RowData> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<RowData> subsections) {
        this.subsections = subsections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RowData)) return false;

        RowData row = (RowData) o;

        if (maxCount != row.maxCount) return false;
        if (!name.equals(row.name)) return false;
        if (!required.equals(row.required)) return false;
        if (!subsections.equals(row.subsections)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + required.hashCode();
        result = 31 * result + maxCount;
        result = 31 * result + subsections.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RowData{" +
                "name='" + name + '\'' +
                ", required=" + required +
                ", maxCount=" + maxCount +
                ", subsections=" + subsections +
                '}';
    }
}
