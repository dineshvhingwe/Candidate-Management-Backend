package com.sndcorp.candidatemanage.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Profession;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.repo.CandidateRepository;
import com.sndcorp.candidatemanage.repo.ProfessionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfessionService {

	@Autowired
	private ProfessionRepository professionRepo;
	@Autowired
	private CandidateRepository candidateRepo;

	public Profession addProfessionFromScratch(Profession profession) {
		log.info("Adding Profession from scratch: {} ", profession);
		return professionRepo.save(profession);
	}
	
	public Profession addProfessionToCandidate(String candidate_id, Profession profession) {
		log.info("Adding Profession:: {} to Candidate:: {} ", profession, candidate_id);
		Candidate candidate = candidateRepo.findById(candidate_id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidate_id));

		profession.setCandidate(candidate);
		return professionRepo.save(profession);
	}

	public List<Profession> findAllProfessions() {
		return professionRepo.findAll();
	}

	public void deleteProfession(String id) {
		professionRepo.deleteById(id);
	}

	public List<Profession> findProfessionByCandidate(String candidateId) {
		Candidate candidate = candidateRepo.getReferenceById(candidateId);
		log.info("Finding profession of {}", candidateId);
		return professionRepo.findByCandidate(candidate);
		// .orElseThrow(() -> new ProfessionNotFoundException("user by email " + email +
		// " was not found"));
	}
}
