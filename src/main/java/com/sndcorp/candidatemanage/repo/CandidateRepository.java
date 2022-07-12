package com.sndcorp.candidatemanage.repo;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sndcorp.candidatemanage.entities.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, String> {
	// spring creates a query by understanding the method using naming convention
	// //delete+ classname + ById
	// it is called query method
	@Transactional
	void deleteCandidateByEmail(String email);

	// query method
	Candidate findCandidateByEmail(String email);

	@Transactional(value = TxType.REQUIRES_NEW)
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE CANDIDATES c SET c.name = :name, c.surname = :surname where c.email = :email", nativeQuery = true)
	void updateNameAndJobTitleByEmail(String name, String surname, String email);

	List<Candidate> findCandidatesByTagsId(Long tagId);

}
