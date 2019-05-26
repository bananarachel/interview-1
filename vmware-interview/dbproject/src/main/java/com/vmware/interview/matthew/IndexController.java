/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/oldindex")
@Api(value = "index", tags = ("index"))
public class IndexController
{
    @Autowired
    private Environment environment;

    @RequestMapping(method = RequestMethod.GET)
    public String getIndex(Model model)
    {

        model.addAttribute("activeProfile", environment.getActiveProfiles());

        return "index";
    }


}
