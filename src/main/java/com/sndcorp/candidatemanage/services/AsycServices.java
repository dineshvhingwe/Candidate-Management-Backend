package com.sndcorp.candidatemanage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.repo.CandidateRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsycServices {

	@Autowired
	private CandidateRepository candidateRepo;

	@Async(value = "candidateManageUpdateExecutor")
	public void updateCandidate(Candidate candidate, String username) {
		log.debug("Started Thread to update the values of Phone and Email", Thread.currentThread().getName());

		Candidate empDb = candidateRepo.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", "Email"));

		log.info("updating candidate values in thread: {} ", Thread.currentThread().getName());
		empDb.setName(candidate.getName());
		empDb.setSurname(candidate.getSurname());
		empDb.setAddress(candidate.getAddress());
		empDb.setBookmarkedCandidates(candidate.getBookmarkedCandidates());
		empDb.setImageUrl(candidate.getImageUrl());
		empDb.setTags(candidate.getTags());
		if (!empDb.getPhone().equalsIgnoreCase(candidate.getPhone())) {
			log.debug("trigger phone approval completed");
		}
		if (!empDb.getEmail().equalsIgnoreCase(candidate.getEmail())) {
			log.debug("triggerMultiFactorAuthEmail completed");
		}
		candidateRepo.save(empDb);
		log.debug("Ending Thread {}. Updated the values of Phone and Email", Thread.currentThread().getName());

	}

}
