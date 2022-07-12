package com.sndcorp.candidatemanage.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sndcorp.candidatemanage.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
	//List<Tag> findTagsByCandidate(Candidate candidate);
	Optional<Tag> findByName(String name);
}