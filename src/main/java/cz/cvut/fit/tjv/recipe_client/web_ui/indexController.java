package cz.cvut.fit.tjv.recipe_client.web_ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {
    @GetMapping("/")
    public String showIndex() {
        return "index";
    }
}
