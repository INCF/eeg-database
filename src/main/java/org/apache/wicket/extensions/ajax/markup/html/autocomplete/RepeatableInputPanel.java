package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import org.apache.commons.validator.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.IValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 9:07
 */
public class RepeatableInputPanel<T extends IAutoCompletable> extends Panel {
    final protected List<T> data = new ArrayList<T>();

    protected IFactory<T> factory;
    protected GenericFacade<T, Integer> service;

    final protected RepeatableListView<T> repeatables;

    public RepeatableInputPanel(String id,

                                final IFactory<T> factory,
                                final IValidator<T> validator,

                                GenericFacade<T, Integer> service) {
        super(id);
        this.factory = factory;
        this.service = service;

        data.add(factory.create());
        setOutputMarkupId(true);

        repeatables =
                new RepeatableListView<T>("repeatableInputList", data, factory, validator, service);
        repeatables.setOutputMarkupId(true);
        repeatables.setReuseItems(true);

        this.add(AttributeModifier.append("class", "repeatable"));

        add(repeatables);
    }

    public List<GenericModel<T>> getData() {
        return repeatables.getData();
    }

    private Panel getThisPanel(){
        return this;
    }

    private class RepeatableListView<T extends IAutoCompletable> extends ListView<T> {
        protected IFactory<T> factory;
        protected IValidator<T> validator;
        protected GenericFacade<T, Integer> service;

        protected List<GenericModel<T>> modelsInner;

        public RepeatableListView(String id,
                                  List<T> data,
                                  IFactory<T> factory,
                                  IValidator<T> validator,
                                  GenericFacade<T, Integer> service) {
            super(id, data);

            modelsInner = new ArrayList<GenericModel<T>>();
            this.factory = factory;
            this.validator = validator;
            this.service = service;
        }

        public List<GenericModel<T>> getData() {
            return modelsInner;
        }

        @Override
        protected void populateItem(ListItem<T> item) {
            T modelObject = item.getModelObject();
            GenericModel<T> model = new GenericModel<T>(modelObject);
            modelsInner.add(model);

            final RepeatableInput<T> repeatable = new RepeatableInput<T>(
                    "repeatableInput",
                    model,
                    factory.getClassForConverter(),
                    service
            );
            repeatable.add(validator);

            ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(repeatable);
            final FeedbackPanel repeatableFeedback = new FeedbackPanel("repeatableFeedback", repeatableFilter);
            repeatableFeedback.setOutputMarkupId(true);

            repeatable.add(new AjaxFormComponentUpdatingBehavior("change") {
                @Override
                protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                    List<T> modelList = (List<T>) getList();
                    boolean addNew = true;
                    for(GenericModel<T> t: modelsInner) {
                        if(t == null || t.getObject() == null || t.getObject().getAutoCompleteData() == null) {
                            addNew = false;
                        }
                    }
                    if(addNew) {
                        modelList.add(factory.create());
                        ajaxRequestTarget.add(getThisPanel());
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, RuntimeException e){
                    super.onError(target, e);
                    target.add(repeatableFeedback);
                }
            });

            repeatable.add(new AjaxFormComponentUpdatingBehavior("onblur") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    ((GenericValidator<T>)validator).setList(getList());
                }
            });
            item.add(repeatableFeedback);

            item.add(repeatable);
        }
    }
}
