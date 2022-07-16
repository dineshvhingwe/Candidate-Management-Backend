package com.sndcorp.candidatemanage.services;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@Transactional(value = TxType.REQUIRES_NEW)
	public void deleteProfession(String id) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Candidate candidate = candidateRepo.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", username));

		log.debug("deleting profession from username: {}", username);
		//delete only if profession exists in that candidate
		professionRepo.findByCandidate(candidate).forEach((profession) -> {
			log.debug("checking profession id equality of {}", profession.getProfession_id());
			if (profession.getProfession_id().equalsIgnoreCase(id)) {
				professionRepo.deleteById(id);
				log.debug("deleted profession successfully {} ", id);
			}
		});
	}

	public List<Profession> findProfessionByCandidate(String candidateId) {
		Candidate candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId));
		log.info("Finding profession of {}", candidateId);
		return professionRepo.findByCandidate(candidate);
	}
}
