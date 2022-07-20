package com.sndcorp.candidatemanage.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.repo.CandidateRepository;
import com.sndcorp.candidatemanage.repo.ProfessionRepository;
import com.sndcorp.candidatemanage.repo.TagRepository;
import com.sndcorp.candidatemanage.security.services.UserDetailsImpl;

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

	@Autowired
	private AsycServices asycServices;
	
	public Candidate addCandidate(Candidate candidate) {
		log.info("Saving :: {} ", candidate);
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("User Obtained from context: {}, {}", user.getUsername(), user);
		// addressRepo.save(candidate.getAddress());
		return candidateRepo.save(candidate);
	}

	public List<Candidate> findAllCandidates(String name, int pageNo, int size, String[] sort) {

		List<Candidate> candidates;
		// for simplicity keep sorting by name for timebeing
		Pageable pageable = PageRequest.of(pageNo, size, Sort.by("name").descending());
		if (name.isBlank()) {
			Page<Candidate> candidatePage = candidateRepo.findAll(pageable);
			candidates = candidatePage.getContent();
			log.debug("Page is :{}", candidatePage);
			return candidates;
		} else {
			Page<Candidate> candidatePage = candidateRepo.findByUsernameContaining(name, pageable);
			candidates = candidatePage.getContent();
			log.debug("Page is :{}", candidatePage);
			return candidates;
		}
	}

	public String updateCandidate(Candidate candidate, String username) {

		userNameSecurityCheck(username);

		log.debug("Starting Thread to update the values of Phone and Email {}", Thread.currentThread().getName());
		asycServices.updateCandidate(candidate, username);

		return username;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteCandidate(String username) {

		professionRepo.deleteByCandidate(candidateRepo.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", username)));
		candidateRepo.deleteByUsername(username);
	}

	public Candidate findCandidateByEmail(String email) {
		return candidateRepo.findCandidateByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", "Email" + email));
	}

	public Candidate findCandidateByUsername(String username) {
		return candidateRepo.findCandidateByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", "Username: " + username));
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

	public void userNameSecurityCheck(String usernameFromRequest) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("UserName Obtained from context: {}", user.getUsername());
		String usernameFromContext = user.getUsername();
		if (!usernameFromContext.equalsIgnoreCase(usernameFromRequest)) {
			throw new SecurityException("Username from Context different than in Request !!");
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteCandidateByCandidateId(String candidateId) {

		professionRepo.deleteByCandidate(candidateRepo.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId)));
		candidateRepo.deleteById(candidateId);
	}

	public void triggerMultiFactorAuthPhone() {

	}

	public void triggerMultiFactorAuthEmail() {

	}
}
