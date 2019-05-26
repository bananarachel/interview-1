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

import java.util.*;

@Controller
@RequestMapping(value = "/customer/detail")
@Api(value = "customer detail", tags = ("customer detail"))
public class CustomerDetailController
{
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String getCustomer(@RequestParam(value = "customerId") Long customerId, Model model)
    {
        try
        {
            Customer customer = null;
            Set<Service> subscribeServices;

            Optional<Customer> optionalCustomer = this.customerRepository.findById(customerId);
            if (optionalCustomer.isPresent())
            {
                customer = optionalCustomer.get();
                subscribeServices = customer.getSubscribedService();
            } else
            {
                return Utils.redirectToError("cannot find customer by id: " + customerId);
            }
            List<Service> allServices = (List<Service>) this.serviceRepository.findAll();
            allServices.removeAll(subscribeServices);
            SubscribeServiceForm form = new SubscribeServiceForm();
            form.setCustomerId(customerId);

            model.addAttribute("customer", customer);
            model.addAttribute("subscribeServices", subscribeServices);
            model.addAttribute("allAvailableServices", allServices);
            model.addAttribute("subscribeServiceForm", form);
            return "customerdetail";
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
            return "redirect:/customer/detail?customerId=" + form.getCustomerId();
        } else
        {
            return Utils.redirectToError("subscribe service failed, Please try it later.");
        }
    }
}
