/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping(value = "/errorinfo")
@Api(value = "error", tags = ("errorinfo"))
public class ErrorInfoController
{
    @RequestMapping(method = RequestMethod.GET)
    public String getError(@PathParam(value = "message") String message, Model model)
    {
        model.addAttribute("info", message);
        return "errorinfo";
    }
}
