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
 *   ExperimentBuyDownloadLinkPanel.java, 2014/13/10 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsDownloadPage;

public class ExperimentBuyDownloadLinkPanel extends Panel {

    private static final long serialVersionUID = 5458856518415845451L;

    @SpringBean
    private OrderFacade facade;

    private Experiment experiment;

    private boolean inCart = false;
    private boolean isDownloadable = false;

    public ExperimentBuyDownloadLinkPanel(String id, IModel<Experiment> model) {
        super(id);
        experiment = model.getObject();
        
        // XXX price hidden for now.
        /*
        add(new Label("price", experiment.getPrice() != null ? experiment.getPrice() : BigDecimal.ZERO) {
            
            private static final long serialVersionUID = 1L;

            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return new MoneyFormatConverter(Currency.getInstance("EUR"), 2);
            }
        });
        */
        add(new Link<Void>("addToCartLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().getShoppingCart().addToCart(experiment);
                setResponsePage(getPage());
            }

            @Override
            public boolean isVisible() {
                return !inCart && !isDownloadable;
            }
        });

        add(new Label("inCart", ResourceUtils.getModel("text.inCart")) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return inCart;
            }

        });

        // "Add to Cart" links are rendered only for experiments that haven't been places in the cart yet.
        BookmarkablePageLink<Void> downloadLink = new BookmarkablePageLink<Void>("downloadLink", ExperimentsDownloadPage.class, PageParametersUtils.getDefaultPageParameters(experiment
                .getExperimentId())) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return isDownloadable;
            }
        };
        add(downloadLink);
    }

    @Override
    protected void onConfigure() {
        inCart = inCart(experiment);
        isDownloadable = isDownloadable(experiment);
    }

    private boolean isDownloadable(final Experiment experiment) {
        return EEGDataBaseSession.get().isExperimentPurchased(experiment.getExperimentId());
    }

    private boolean inCart(final Experiment experiment) {
        return EEGDataBaseSession.get().getShoppingCart().isInCart(experiment);
    }

}
