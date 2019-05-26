/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import com.vmware.interview.matthew.domain.Customer;
import com.vmware.interview.matthew.domain.Service;
import com.vmware.interview.matthew.form.SubscribeServiceForm;
import com.vmware.interview.matthew.repository.CustomerRepository;
import com.vmware.interview.matthew.repository.ServiceRepository;
import com.vmware.interview.matthew.repository.SubscribeRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(value = "/service/detail")
@Api(value = "service/detail", tags = ("service"))
public class ServiceDetailController
{
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String getServiceById(@RequestParam(value = "serviceId") Long serviceId, Model model)
    {
        try
        {
            Service service = null;
            Set<Customer> customers = null;

            Optional<Service> optionalService = this.serviceRepository.findById(serviceId);
            if (optionalService.isPresent())
            {
                service = optionalService.get();
                customers = service.getCustomers();
            } else
            {
                return "redirect:/error";
            }

            List<Customer> allCustomers = (List<Customer>) this.customerRepository.findAll();
            allCustomers.removeAll(customers);
            SubscribeServiceForm form = new SubscribeServiceForm();
            form.setSubscribeServiceId(serviceId);
            model.addAttribute("service", service);
            model.addAttribute("allCustomers", customers);
            model.addAttribute("availableCustomer", allCustomers);
            model.addAttribute("subscribeServiceForm", form);
            return "servicedetail";
        } catch (Exception e)
        {
            Utils.writeLog(e);
            return Utils.redirectToError(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String subscribeService(@ModelAttribute SubscribeServiceForm form)
    {
        if (form.getSubscribeServiceId() == null || form.getCustomerId() == null)
        {
            return Utils.redirectToError("service id or customer id cannot be null.");
        }
        if (SubscribeRepository.subscribe(this.customerRepository, this.serviceRepository, form))
        {
            return "redirect:/service/detail?serviceId=" + form.getSubscribeServiceId();
        } else
        {
            return "redirect:/error";
        }


    }
}
