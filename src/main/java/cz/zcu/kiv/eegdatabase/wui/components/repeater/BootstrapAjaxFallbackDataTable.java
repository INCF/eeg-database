/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
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
 * BootstrapAjaxFallbackDataTable.java, 15. 6. 2015 11:01:01, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.repeater;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;

/**
 * A Bootstrap-styled implementation of the DataTable that adds navigation,
 * headers, an no-records-found toolbars to a standard {@link DataTable}.
 * <p>
 * The {@link HeadersToolbar} is added as a top toolbar, while the {@link NavigationToolbar}
 * and the {@link NoRecordsToolbar} toolbars are added as bottom toolbars.
 * </p>
 * 
 * @see DataTable
 * @see HeadersToolbar
 * @see NavigationToolbar
 * @see NoRecordsToolbar
 * 
 * @author Jakub Krauz
 * 
 * @param <T>
 *     the model object type
 * @param <S>
 *     the type of the sorting parameter
 * 
 */
public class BootstrapAjaxFallbackDataTable<T, S> extends DataTable<T, S> {

    private static final long serialVersionUID = 1L;


    /**
     * Constructor
     * 
     * @param id
     *            component id
     * @param columns
     *            list of IColumn objects
     * @param dataProvider
     *            imodel for data provider
     * @param rowsPerPage
     *            number of rows per page
     */
    public BootstrapAjaxFallbackDataTable(final String id,
                                          final List<? extends IColumn<T, S>> columns,
                                          final ISortableDataProvider<T, S> dataProvider,
                                          final int rowsPerPage) {
        super(id, columns, dataProvider, rowsPerPage);
        setOutputMarkupId(true);
        setVersioned(false);
        addTopToolbar(new AjaxFallbackHeadersToolbar<S>(this, dataProvider));
        addBottomToolbar(new BootstrapAjaxNavigationToolbar(this));
        addBottomToolbar(new NoRecordsToolbar(this));
    }
    

    /** {@inheritDoc} */
    @Override
    protected Item<T> newRowItem(final String id, final int index,
            final IModel<T> model) {
        return new OddEvenItem<T>(id, index, model);
    }


}
