package com.sndcorp.candidatemanage.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sndcorp.candidatemanage.entities.Address;

public interface AddressRepository extends JpaRepository<Address,String> {
    //spring creates a query by understanding the method using naming convention //delete+ classname + ById
    //it is called query method

}
