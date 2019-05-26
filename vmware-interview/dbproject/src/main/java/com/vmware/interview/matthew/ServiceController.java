/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import com.vmware.interview.matthew.domain.Service;
import com.vmware.interview.matthew.form.ServiceForm;
import com.vmware.interview.matthew.repository.ServiceRepository;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/service")
@Api(value = "service", tags = ("service"))
public class ServiceController
{
    @Autowired
    private ServiceRepository serviceRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String getServices(Model model)
    {
        try
        {
            List<Service> services;

            services = (List<Service>) this.serviceRepository.findAll();
            model.addAttribute("allServices", services);
            ServiceForm form = new ServiceForm();
            model.addAttribute("serviceForm", form);
            return "service";
        } catch (Exception e)
        {
            Utils.writeLog(e);
            return Utils.redirectToError(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String crateServices(@ModelAttribute ServiceForm form)
    {
        if (StringUtils.isEmpty(form.getServiceName()) || StringUtils.isEmpty(form.getServiceInfo()))
        {
            return Utils.redirectToError("service name or info cannot be empty.");
        }
        try
        {
            Service service = new Service();
            service.setServiceName(form.getServiceName());
            service.setServiceInfo(form.getServiceInfo());

            this.serviceRepository.save(service);
            return "redirect:/service";
        } catch (Exception e)
        {
            Utils.writeLog(e);
            return Utils.redirectToError(e.getMessage());
        }

    }
}
