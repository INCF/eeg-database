package cz.zcu.kiv.eegdatabase.wui.ui.experiments.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.template.PackageTextTemplate;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.signal.ChannelInfo;
import cz.zcu.kiv.eegdatabase.logic.signal.EegReader;
import cz.zcu.kiv.eegdatabase.logic.signal.VhdrReader;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.DataFileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

/**
 * Panel with painting signals from experiment. It's prototype.
 * 
 * @author Jakub Rinkes
 *
 */
public class ExperimentSignalViewCanvasPanel extends WebMarkupContainer {

    private static final long serialVersionUID = 6848826477541304726L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    FileFacade fileFacade;

    private Experiment experiment;

    public ExperimentSignalViewCanvasPanel(String id, Experiment experiment) {
        super(id);
        this.experiment = experiment;
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {

        // metoda generuje html vystup

        // template je zde nactena sablona ze souboru v tomto package. Obsahuje
        // html + javascript a promenne kam se budou vkladat data. Promenna ma
        // tvar: ${nazevPromenne}
        PackageTextTemplate template = new PackageTextTemplate(this.getClass(), "view.tpl");

        // mapa promenych pro interpolaci se sablonou. Interpolace nahradi
        // promene  ${nazevPromenne} tim co najde v mape pod nazvem promenne.
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("toggle.text", ResourceUtils.getString("text.visualization.toggle"));

        Boolean filesIn = new Boolean(false);
        StringBuilder tree = new StringBuilder();
        StringBuilder generateddata = new StringBuilder();

        // puvodni cyklus pro generovani dat pro vizualizaci signalu. Misto
        // vkladani do modelu controlleru se data generuji jako string, ktery je
        // pak vlozen misto promenne v sablone.
        VhdrReader vhdr = new VhdrReader();
        List<ChannelInfo> channels = null;
        byte[] bytes = null;
        byte[] data = null;
        ArrayList<double[]> signalData = new ArrayList<double[]>();
        for (DataFile file : experiment.getDataFiles()) {
            if (file.getFilename().endsWith(".vhdr")) {
                DataFileDTO fileDto = fileFacade.getFile(file.getDataFileId());
                bytes = fileDto.getFileContent();
                vhdr.readVhdr(bytes);
                channels = vhdr.getChannels();
                tree.append(getTree(channels));
                for (DataFile file2 : experiment.getDataFiles()) {
                    if ((file2.getFilename().endsWith(".eeg")) || (file2.getFilename().endsWith(".avg"))) {
                        filesIn = true;

                        // v controlleru se v tehle fazi cetl misto file2 ten
                        // file ktery je z horniho cyklu. Coz bude asi bug.
                        DataFileDTO fileDto2 = fileFacade.getFile(file2.getDataFileId());
                        data = fileDto2.getFileContent();
                        EegReader eeg = new EegReader(vhdr);
                        for (ChannelInfo ch : channels) {
                            signalData.add(eeg.readFile(data, ch.getNumber()));
                        }
                        generateddata.append(getData(signalData));
                    }
                }
            }
        }

        // nastaveni viditelnosti
        this.setVisibilityAllowed(filesIn);

        parameters.put("generate.tree", tree.toString());
        parameters.put("generate.data", generateddata.toString());
        // provedeni interpolaci promenych a sablony a vygenerovani html
        // vystupu.
        getResponse().write(template.asString(parameters));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        // tahle metoda prinuti wicket dodat do hlavicky stranky scripty co jsou
        // potreba.
        // pridany scripty pro vizualizaci signalu a wicket jquery knihovny.
        response.render(JavaScriptHeaderItem.forUrl("/files/js/visualization/browserControl.js"));
        response.render(JavaScriptHeaderItem.forUrl("/files/js/visualization/visualization.js"));
        response.render(JavaScriptHeaderItem.forUrl("/files/js/visualization/dhtmlxcommon.js"));
        response.render(JavaScriptHeaderItem.forUrl("/files/js/visualization/dhtmlxtree.js"));
        IJavaScriptLibrarySettings settings = EEGDataBaseApplication.get().getJavaScriptLibrarySettings();
        response.render(JavaScriptHeaderItem.forReference(settings.getJQueryReference()));
        response.render(JavaScriptHeaderItem.forReference(settings.getWicketAjaxReference()));
        super.renderHead(response);
    }

    private String getTree(List<ChannelInfo> channels) {

        StringBuilder output = new StringBuilder();
        int counter = 1;
        for (ChannelInfo info : channels) {
            output.append("tree.insertNewChild(1, 10 + " + counter + ",\"" + info.getName() + "\");\n");
            counter++;
        }

        return output.toString();
    }

    private String getData(ArrayList<double[]> signalData) {

        StringBuilder output = new StringBuilder();
        int counter = 0;
        int counter2 = 0;

        for (double[] tmp : signalData) {
            output.append("createArray(" + (counter + 1) + ");\n");
            for (double d : tmp) {
                output.append("addToArray(" + (counter + 1) + "," + (counter2 + 1) + "," + tmp[counter2] + ");\n");
                counter2++;
            }
            counter++;
            counter2 = 0;
        }
        return output.toString();
    }

}
