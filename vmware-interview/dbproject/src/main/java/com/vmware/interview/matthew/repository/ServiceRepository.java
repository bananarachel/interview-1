/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

package com.vmware.interview.matthew.repository;

import com.vmware.interview.matthew.domain.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Service repository
 */
@Repository
public interface ServiceRepository extends CrudRepository<Service, Long>
{
    Service findByServiceName(String serviceName);

    List<Service> findByCustomers_id(Long customerId);
}
