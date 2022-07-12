package com.sndcorp.candidatemanage.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Profession;

public interface ProfessionRepository extends JpaRepository<Profession,String> {
    //spring creates a query by understanding the method using naming convention //delete+ classname + ById	
	List<Profession> findByCandidate(Candidate candidate);
}
