package com.sndcorp.candidatemanage.repo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Tag;

public interface CandidateRepository extends JpaRepository<Candidate, String> {
	
	Optional<Candidate> findByUsername(String username);
	
	@Transactional
	void deleteByUsername(String username);

	// query method
	Optional<Candidate> findCandidateByEmail(String email);

	Optional<Candidate> findCandidateByUsername(String username);
	
	@Transactional(value = TxType.REQUIRES_NEW)
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE CANDIDATES c SET c.name = :name, c.surname = :surname where c.email = :email", nativeQuery = true)
	void updateNameAndJobTitleByEmail(String name, String surname, String email);

	List<Candidate> getCandidatesByTags(Tag tag);
	
	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

}
