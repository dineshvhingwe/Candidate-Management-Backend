package com.sndcorp.candidatemanage.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Tag;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.repo.CandidateRepository;
import com.sndcorp.candidatemanage.repo.ProfessionRepository;
import com.sndcorp.candidatemanage.repo.TagRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CandiateService {

	@Autowired
	private CandidateRepository candidateRepo;
	@Autowired
	private TagRepository tagRepo;
	@Autowired
	private ProfessionRepository professionRepo;

	public Candidate addCandidate(Candidate candidate) {
		log.info("Saving :: {} ", candidate);
		// addressRepo.save(candidate.getAddress());
		return candidateRepo.save(candidate);
	}

	public List<Candidate> findAllCandidates() {
		return candidateRepo.findAll();
	}

	public Candidate updateCandidate(Candidate candidate) {
		Candidate empDb = candidateRepo.findCandidateByEmail(candidate.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", "Email"));

		log.info("updating values: {} , {}", candidate.getSurname(), candidate.getName());
		empDb.setName(candidate.getName());
		empDb.setSurname(candidate.getSurname());
		candidateRepo.save(empDb);
		return empDb;

	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void deleteCandidate(String id) {

		professionRepo.deleteByCandidate(
				candidateRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate", id)));
		candidateRepo.deleteById(id);
	}

	public Candidate findCandidateByEmail(String email) {
		return candidateRepo.findCandidateByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", "Email" + email));
	}

	public Candidate addOrRemoveBookmarkToCandidate(String candidate_id, String bookmark_id) {
		Candidate candidate = candidateRepo.findById(candidate_id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidate_id));
		Set<UUID> bookmarks = candidate.getBookmarkedCandidates();
		if (bookmarks.contains(UUID.fromString(bookmark_id))) {
			log.debug("Bookmark already exists. Removing {}", bookmark_id);
			bookmarks.remove(UUID.fromString(bookmark_id));
			candidate.setBookmarkedCandidates(bookmarks);
			return candidateRepo.save(candidate);
		}
		log.debug("Bookmark doesnt exists. Adding {}", bookmark_id);
		bookmarks.add(UUID.fromString(bookmark_id));
		candidate.setBookmarkedCandidates(bookmarks);
		return candidateRepo.save(candidate);

	}

	public List<Candidate> getCandidatesByTagId(Long tagId) {

		Tag tag = tagRepo.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag", tagId));
		return candidateRepo.getCandidatesByTags(tag);

	}
}
