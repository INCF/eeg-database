package cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection;

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
public class SectionType implements Serializable{

    //section name
    private final String name;
    //is section required?
    private final Boolean required;
    //Max section count
    private final int maxCount;
    //Min section count
    private final int minCount;
    //List of possible subsections
    private final List<SectionType> subsections;
    //Selected count of sections
    private int selectedCount;
    //Is section selected?
    private boolean selected;

    public SectionType(String name, Boolean required, int maxCount, List<SectionType> subsections, int minCount,
                       int selectedCount, boolean selected) {
        this.name = name;
        this.required = required;
        this.maxCount = maxCount;
        this.subsections = subsections;
        this.minCount = minCount;
        this.selectedCount = selectedCount;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public Boolean getRequired() {
        return required;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public List<SectionType> getSubsections() {
        return subsections;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public int getSelectedCount() {
        return selectedCount;
    }
}
