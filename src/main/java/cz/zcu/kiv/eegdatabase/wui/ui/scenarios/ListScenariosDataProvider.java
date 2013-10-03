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
 *   ListScenariosDataProvider.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.scenarios;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;

/**
 * Dataprovider implementation used in table on ListScenarioPage.
 * 
 * @author Jakub Rinkes
 *
 */
public class ListScenariosDataProvider extends SortableDataProvider<Scenario, String> {

    private static final long serialVersionUID = 3555979400074686801L;

    protected Log log = LogFactory.getLog(getClass());

    ScenariosFacade facade;

    private List<Scenario> list;

    private int size;

    public ListScenariosDataProvider(ScenariosFacade facade) {

        this.facade = facade;
        setSort("scenarioId", SortOrder.ASCENDING);

        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        size = facade.getScenarioCountForList(loggedUser);
        list = facade.getScenariosForList(loggedUser, 0, size);

    }

    @Override
    public Iterator<? extends Scenario> iterator(long first, long count) {

        if (getSort() != null)
            Collections.sort(list, new ScenariosDataProviderComparator());

        if (size() < first + count)
            list.subList((int) first, (int) (first + size() - first)).iterator();

        return list.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public IModel<Scenario> model(Scenario object) {
        return new Model<Scenario>(object);
    }

    private class ScenariosDataProviderComparator implements Comparator<Scenario>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        public int compare(final Scenario o1, final Scenario o2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, getSort().getProperty());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty());

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
