package cz.zcu.kiv.eegdatabase.wui.ui.people;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.StyleClassPropertyColumn;
import cz.zcu.kiv.eegdatabase.wui.components.table.ViewLinkPanel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

@AuthorizeInstantiation("ROLE_USER")
public class ListPersonPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    private static final int ITEMS_PER_PAGE = 20;

    @SpringBean
    PersonFacade personFacade;

    public ListPersonPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.listOfPeople"));
        setupComponents();
    }

    private void setupComponents() {

        add(new ButtonPageMenu("leftMenu", PersonPageLeftMenu.values()));

        DefaultDataTable<Person, String> list = new DefaultDataTable<Person, String>("list", createListColumns(),
                new ListPersonDataProvider(personFacade), ITEMS_PER_PAGE);

        add(list);

    }

    private List<? extends IColumn<Person, String>> createListColumns() {
        List<IColumn<Person, String>> columns = new ArrayList<IColumn<Person, String>>();

        columns.add(new StyleClassPropertyColumn<Person, String>(ResourceUtils.getModel("dataTable.heading.surname"), "surname", "surname", "width100"));
        columns.add(new StyleClassPropertyColumn<Person, String>(ResourceUtils.getModel("dataTable.heading.name"), "givenname", "givenname", "width100"));
        columns.add(new PropertyColumn<Person, String>(ResourceUtils.getModel("dataTable.heading.note"), "note", "note") {

            private static final long serialVersionUID = 1L;

            @Override
            public IModel<String> getDisplayModel() {

                IModel<String> displayModel = super.getDisplayModel();

                if (displayModel.getObject().length() < 70)
                    return displayModel;

                return new Model<String>(displayModel.getObject().substring(0, 70));
            }
        });
        // TODO person detail page
        columns.add(new StyleClassPropertyColumn<Person, String>(ResourceUtils.getModel("dataTable.heading.detail"), null, null, "width50") {

            private static final long serialVersionUID = 1L;

            @Override
            public void populateItem(Item<ICellPopulator<Person>> item, String componentId, IModel<Person> rowModel) {
                item.add(new ViewLinkPanel(componentId, UnderConstructPage.class, "personId", rowModel, ResourceUtils.getModel("link.detail")));
            }
        });

        return columns;
    }
}
