package cz.zcu.kiv.eegdatabase.logic.controller.searchsolr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 11.3.13
 * Time: 23:29
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class FullTextSearchController {

    @RequestMapping("solrsearch/search")
    public void search() {
        System.out.println("I'm in search!");
    }
}
