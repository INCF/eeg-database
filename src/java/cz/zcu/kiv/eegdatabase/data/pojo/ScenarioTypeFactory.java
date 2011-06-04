package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 31.5.11
 * Time: 20:12
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioTypeFactory {
    public static ScenarioType createScenarioType(int id) {
        switch (id) {
            case 0:
                return new ScenarioTypeNonSchema();
            case 1:
                return new ScenarioTypeSchema1();
            case 2:
                return new ScenarioTypeSchema2();
            case 3:
                return new ScenarioTypeSchema3();
            /*
            case 4:
                return new ScenarioTypeSchema4();
            break;
            case 5:
                return new ScenarioTypeSchema5();
            break;
            case 6:
                return new ScenarioTypeSchema6();
            break;
            case 7:
                return new ScenarioTypeSchema7();
            break;
            case 8:
                return new ScenarioTypeSchema8();
            break;
            */
            default:
                return new ScenarioTypeNonXml();
        }
    }
}
