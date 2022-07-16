package com.sndcorp.candidatemanage.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findByName(String name);
	
	boolean existsByName(String tag_name);
	
	@Query(value = "SELECT TAG_ID FROM candidates_tags WHERE candidate_id = :candidateId", nativeQuery = true)
	List<Long> getTagIdsByCandidateId(String candidateId);
	
	List<Tag> findTagsByCandidates(Candidate cndidate);
	
	void deleteById(Long tag_id);
}