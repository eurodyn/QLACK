package com.eurodyn.qlack.fuse.demo.war.ws;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author European Dynamics
 */

@Controller
public class PageWS {

    @GetMapping("/login")
    public ModelAndView login(Model model) {
        ModelAndView m = new ModelAndView("login");
        return m;
    }

    @GetMapping("/operations")
    public ModelAndView details() {
        ModelAndView model = new ModelAndView("operations");
        return model;
    }

}

