package ch.usi.inf.sa4.sphinx.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("")
public class DocsController {
    @GetMapping("/docs")
    public ModelAndView redirectToSwaggerDocs(ModelMap model) {
        //model.addAttribute("attribute", "MY_ATTRIBUTE"); //use this to set extra attributes if needed
        return new ModelAndView("redirect:/swagger-ui.html", model);
    }

}