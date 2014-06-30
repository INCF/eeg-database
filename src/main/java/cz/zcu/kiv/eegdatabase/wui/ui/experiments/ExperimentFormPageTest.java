package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.SectionCell;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm.RowData;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.include.Include;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.transformer.XsltOutputTransformerContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.util.*;

/**
 * Created by Prokop on 11.6.2014.
 */
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ExperimentFormPageTest extends MenuPage {

    public ExperimentFormPageTest() {
        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));
        List<RowData> rowData = generateData();
        Form<List<RowData>> form = new Form<List<RowData>>("form", new CompoundPropertyModel<List<RowData>>(rowData));
        List<String> list = Arrays.asList("Subsec1", "Subsec2", "Subsec3");
        CheckBoxMultipleChoice<String> cbmc = new CheckBoxMultipleChoice<String>("checkGroup",
                new ListModel<String>(new ArrayList<String>()), list);
        form.add(new TextField<String>("textField", Model.of("")));
        form.add(cbmc);

        ListView view = new PropertyListView("row", rowData) {
            @Override
            protected void populateItem(ListItem item) {
                SectionCell sectionCell = new SectionCell("cell1", item.getModel());
                SectionCell secondCell= new SectionCell("cell2", item.getModel());
                item.add(sectionCell);
                item.add(secondCell);
            }
        };
        form.add(view);
        add(form);
    }

    private List<RowData> generateData() {
        List<RowData> list = new ArrayList<RowData>();
        RowData data;
        RowData subsec;
        String name;
        Boolean required;
        int maxCount;
        List<RowData> subsections;

        for(int i = 0; i<10; i++){
            name = "name " + i;
            required = i%2==0;
            maxCount = i%4 + 1;
            subsections = new ArrayList<RowData>();

            for(int j = i; j < 6; j++){
                subsec = new RowData("subsec"+j, j%2==0,i%4, null);
                subsections.add(subsec);
            }
            data = new RowData(name, required, maxCount, subsections);
            list.add(data);
        }

        return list;
    }

    private void setupComponents(final Model<Experiment> model) {
        XsltOutputTransformerContainer xsltCon = new XsltOutputTransformerContainer("testContent", model,
                "files/odML/Selection.xsl");


        Include inc = new Include("testContent", "files/odML/Sections.xml");
        inc.setEscapeModelStrings(false);

        add(new XsltOutputTransformerContainer("testContent", model,
                "files/odML/Selection.xsl"));
    }
}
