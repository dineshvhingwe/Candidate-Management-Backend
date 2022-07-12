package com.sndcorp.candidatemanage.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.repo.CandidateRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CandiateService {

	@Autowired
	private CandidateRepository candidateRepo;

	public Candidate addCandidate(Candidate candidate) {
		log.info("Saving :: {} ", candidate);
		// addressRepo.save(candidate.getAddress());
		return candidateRepo.save(candidate);
	}

	public List<Candidate> findAllCandidates() {
		return candidateRepo.findAll();
	}

	public Candidate updateCandidate(Candidate candidate) {
		Candidate empDb = candidateRepo.findCandidateByEmail(candidate.getEmail());
		if (ObjectUtils.isEmpty(empDb)) {
			throw new ResourceNotFoundException("Candidate", candidate.getEmail() );
		} else {
			log.info("updating values: {} , {}", candidate.getSurname(), candidate.getName());
			empDb.setName(candidate.getName());
			empDb.setSurname(candidate.getSurname());
			candidateRepo.save(empDb);
			return candidateRepo.findCandidateByEmail(candidate.getEmail());
		}
	}

	public void deleteCandidate(String email) {
		candidateRepo.deleteCandidateByEmail(email);
	}

	public Candidate findCandidateByEmail(String email) {
		return candidateRepo.findCandidateByEmail(email);
		// .orElseThrow(() -> new CandidateNotFoundException("user by email " + email +
		// " was not found"));
	}
}
