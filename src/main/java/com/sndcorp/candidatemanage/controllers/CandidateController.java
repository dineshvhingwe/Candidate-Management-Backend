package com.sndcorp.candidatemanage.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.services.CandiateService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CandidateController {

	@Autowired
	private CandiateService candidateService;

	@GetMapping("/candidates")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Candidate>> getAllCandidates() {
		List<Candidate> Candidates = candidateService.findAllCandidates();
		return new ResponseEntity<>(Candidates, HttpStatus.OK);
	}

	@GetMapping("/candidate/{username}")
	public ResponseEntity<Candidate> getCandidateByUsername(@PathVariable("username") String username) {
		Candidate Candidate = candidateService.findCandidateByUsername(username);
		return new ResponseEntity<>(Candidate, HttpStatus.OK);
	}

	@PostMapping(value = "/candidate", consumes = "application/json")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Candidate> addCandidate(@RequestBody @Valid Candidate Candidate) {
		Candidate newCandidate = candidateService.addCandidate(Candidate);
		return new ResponseEntity<>(newCandidate, HttpStatus.CREATED);
	}

	@PutMapping("/candidate/{username}")
	public ResponseEntity<String> updateCandidate(@PathVariable("username") String username, @RequestBody @Valid Candidate candidate) {
		username = candidateService.updateCandidate(candidate, username);
		return new ResponseEntity<>("updateCandidate request raised for username: "+ username, HttpStatus.OK);
	}

	@DeleteMapping("/candidate/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteCandidateByUsername(@PathVariable("username") String username) {
		candidateService.deleteCandidate(username);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@DeleteMapping("/candidate/candidateId/{candidateId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteCandidateByCandidateId(@PathVariable("candidateId") String candidateId) {
		candidateService.deleteCandidateByCandidateId(candidateId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/candidate/{candidate_id}/bookmark/{bookmark_id}")
	public ResponseEntity<Candidate> addBookmarkToCandidate(@PathVariable("candidate_id") String candidate_id, @PathVariable("bookmark_id") String bookmark_id ) {
		Candidate candidate = candidateService.addOrRemoveBookmarkToCandidate(candidate_id, bookmark_id);
		return new ResponseEntity<>(candidate, HttpStatus.CREATED);
	}
	
}
