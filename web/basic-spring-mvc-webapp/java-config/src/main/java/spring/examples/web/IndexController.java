package spring.examples.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author arwan.kr@gmail.com
 */
@Controller
public class IndexController {

    @Value("${app.name}")
    private String appName;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("appName", appName);

        return "index";
    }

}
