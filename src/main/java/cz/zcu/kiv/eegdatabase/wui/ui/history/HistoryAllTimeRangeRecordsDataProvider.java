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
 *   HistoryAllTimeRangeRecordsDataProvider.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.history;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.wui.core.history.HistoryFacade;

// TODO change extended paretn class - use generic provider from master
public class HistoryAllTimeRangeRecordsDataProvider extends SortableDataProvider<History, String> {

    private static final long serialVersionUID = -4981823056148067437L;

    private HistoryFacade facade;

    private List<History> list;

    private int size;

    public HistoryAllTimeRangeRecordsDataProvider(HistoryFacade facade) {
        this.facade = facade;
    }

    public void setData(ChoiceHistory history, boolean isGroupAdmin, int groupId) {

        list = facade.getHistory(history, isGroupAdmin, groupId);
        size = list.size();
    }

    public int getCountOfHistory() {
        return size;
    }

    @Override
    public Iterator<? extends History> iterator(long first, long count) {
        if (getSort() != null)
            Collections.sort(list, new HistoryLastDownloadedDataProviderComparator());

        if (size() < first + count)
            return list.subList((int) first, (int) (first + size() - first)).iterator();

        return list.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public IModel<History> model(History object) {
        return new Model<History>(object);
    }

    private class HistoryLastDownloadedDataProviderComparator implements Comparator<History>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        public int compare(final History h1, final History h2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(h1, getSort().getProperty());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(h2, getSort().getProperty());

            int result = 0;

            if (model1.getObject() == null)
                result = -1;
            else if (model2.getObject() == null)
                result = 1;
            else if (model1.getObject() != null && model2.getObject() != null)
                result = model1.getObject().compareTo(model2.getObject());

            if (!getSort().isAscending()) {
                result = -result;
            }

            return result;
        }

    }

}
