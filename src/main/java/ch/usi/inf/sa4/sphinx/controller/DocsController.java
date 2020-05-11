package ch.usi.inf.sa4.sphinx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


@RestController
@RequestMapping("")
public class DocsController {

    private DocsController() {}
    
    @GetMapping("/docs")
    public static ModelAndView redirectToSwaggerDocs(final Map<String, ?> model) {
        //model.addAttribute("attribute", "MY_ATTRIBUTE"); //use this to set extra attributes if needed
        return new ModelAndView("redirect:/swagger-ui.html", model);
    }

}