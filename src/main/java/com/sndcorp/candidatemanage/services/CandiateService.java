package com.sndcorp.candidatemanage.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Tag;
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

	public Candidate addCandidate(Candidate candidate) {
		log.info("Saving :: {} ", candidate);
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("User Obtained from context: {}, {}", user.getUsername(), user);
		// addressRepo.save(candidate.getAddress());
		return candidateRepo.save(candidate);
	}

	public List<Candidate> findAllCandidates() {
		return candidateRepo.findAll();
	}

	public String updateCandidate(Candidate candidate, String username) {
		
		userNameSecurityCheck(username);
		
		Runnable runnable = () -> {
		Candidate empDb = candidateRepo.findCandidateByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", "Email"));

		log.info("updating candidate values in thread: {} ", Thread.currentThread().getName());
		empDb.setName(candidate.getName());
		empDb.setSurname(candidate.getSurname());
		empDb.setAddress(candidate.getAddress());
		empDb.setBookmarkedCandidates(candidate.getBookmarkedCandidates());
		empDb.setImageUrl(candidate.getImageUrl());
		empDb.setTags(candidate.getTags());
		if( ! empDb.getPhone().equalsIgnoreCase(candidate.getPhone())) {
			triggerMultiFactorAuthPhone();
			
		}
		if( ! empDb.getEmail().equalsIgnoreCase(candidate.getEmail())) {
			triggerMultiFactorAuthEmail();

		}
		candidateRepo.save(empDb);
		};

		log.debug("Starting Thread to update the values of Phone and Email {}",  Thread.currentThread().getName());
		 new Thread(runnable).start();
		log.debug("Started Thread to update the values of Phone and Email");

		return username;
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

	public List<Candidate> getCandidatesByTagId(Long tagId) {

		Tag tag = tagRepo.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag", tagId));
		return candidateRepo.getCandidatesByTags(tag);

	}

	public void userNameSecurityCheck(String usernameFromRequest) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("UserName Obtained from context: {}", user.getUsername());
		String usernameFromContext = user.getUsername();
		if (!usernameFromContext.equalsIgnoreCase(usernameFromRequest)) {
			throw new SecurityException("Username from Context different than in Request !!");
		}
	}
	
	public void triggerMultiFactorAuthPhone() {
		
	}
	
	public void triggerMultiFactorAuthEmail() {
		
	}
}
