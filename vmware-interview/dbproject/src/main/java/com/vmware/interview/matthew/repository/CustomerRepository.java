/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.repository;

import com.vmware.interview.matthew.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Customer repository to get and save data from table customer
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>
{
    Customer findByCustomerName(String customerName);

    List<Customer> findBySubscribedService_id(Long serviceId);
}
