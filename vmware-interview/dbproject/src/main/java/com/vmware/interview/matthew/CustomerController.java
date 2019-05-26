/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew;

import com.vmware.interview.matthew.domain.Customer;
import com.vmware.interview.matthew.form.CustomerForm;
import com.vmware.interview.matthew.repository.CustomerRepository;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Controller for customer.html
 */
@Controller
@RequestMapping(value = "/customer")
@Api(value = "customer", tags = ("customer"))
public class CustomerController
{
    @Autowired
    private CustomerRepository customerRepository;


    @RequestMapping(method = RequestMethod.GET)
    public String getCustomer(Model model)
    {
        try
        {
            List<Customer> customers = (List<Customer>) this.customerRepository.findAll();
            CustomerForm form = new CustomerForm();

            model.addAttribute("allCustomer", customers);
            model.addAttribute("customerForm", form);
            return "customer";
        } catch (Exception e)
        {
            Utils.writeLog(e);
            return Utils.redirectToError(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createCustomer(@ModelAttribute CustomerForm form)
    {
        // Check
        if (StringUtils.isEmpty(form.getCustomerName()) || StringUtils.isEmpty(form.getCustomerInfo()))
        {
            return Utils.redirectToError("customer name or info cannot be empty");
        }
        try
        {
            Customer customer = new Customer();
            customer.setCustomerName(form.getCustomerName());
            customer.setCustomerInfo(form.getCustomerInfo());
            this.customerRepository.save(customer);
            return "redirect:/customer";

        } catch (Exception e)
        {
            Utils.writeLog(e);
            return Utils.redirectToError(e.getMessage());
        }

    }
}
