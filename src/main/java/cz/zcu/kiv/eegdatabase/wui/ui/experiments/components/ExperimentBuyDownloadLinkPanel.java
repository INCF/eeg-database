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

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentLicence;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.logic.controller.experiment.MetadataCommand;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentDownloadProvider;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.order.OrderFacade;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashMap;
import java.util.Set;

public class ExperimentBuyDownloadLinkPanel extends Panel {

    private static final long serialVersionUID = 5458856518415845451L;

    @SpringBean
    private OrderFacade facade;

    @SpringBean
    ExperimentDownloadProvider downloadProvider;

    private IModel<ExperimentLicence> model;
    private Experiment experiment;

    private boolean inCart = false;
    private boolean isDownloadable = false;

    
    public ExperimentBuyDownloadLinkPanel(String id, final Experiment experiment, IModel<ExperimentLicence> model) {
        super(id);
        this.experiment = experiment;
        this.model = model;
        
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
        
        // "Add to cart" link
        // rendered only for experiments that haven't been placed in the cart yet
        add(new Link<Void>("addToCartLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().getShoppingCart().addToCart(ExperimentBuyDownloadLinkPanel.this.model.getObject());
                setResponsePage(getPage());
            }

            @Override
            public boolean isVisible() {
                return (ExperimentBuyDownloadLinkPanel.this.model.getObject() != null)
                        && !inCart && !isDownloadable;
            }
            
            @Override
            public boolean isEnabled() {
                return (ExperimentBuyDownloadLinkPanel.this.model.getObject() != null);
            }
            
        });

        
        // label showing that the experiment is already in the cart
        add(new Label("inCart", ResourceUtils.getModel("text.inCart")) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return inCart;
            }

        });

        
        // "Download" link for purchased experiments
//        BookmarkablePageLink<ExperimentsDownloadPage> downloadLink =
//                new BookmarkablePageLink<ExperimentsDownloadPage>("downloadLink", ExperimentsDownloadPage.class,
//                            PageParametersUtils.getDefaultPageParameters(experiment.getExperimentId())) {
//
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public boolean isVisible() {
//                return isDownloadable;
//            }
//        };
//        add(downloadLink);
        Link<Void> downloadLink = new Link<Void>("downloadLink") {
            @Override
            public void onClick() {
                MetadataCommand command = new MetadataCommand();
                command.setScenario(true);
                FileDTO outputFile = downloadProvider.generate(experiment, command, experiment.getDataFiles(),
                        new HashMap<Integer, Set<FileMetadataParamVal>>());

                if (outputFile == null || outputFile.getFile() == null)
                    error("Error while file is generated. Can't be downloaded.");
                else {
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(FileUtils.prepareDownloadFile(outputFile));
                }
            }
            @Override
            public boolean isVisible() {
                return isDownloadable;
            }
        };
        add(downloadLink);
    }
    
    
    public void setModelObject(ExperimentLicence experimentLicence) {
        model.setObject(experimentLicence);
    }
    
    
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
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
