package com.sndcorp.candidatemanage.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Candidate>> getAllCandidates() {
		List<Candidate> Candidates = candidateService.findAllCandidates();
		return new ResponseEntity<>(Candidates, HttpStatus.OK);
	}

	@GetMapping("/candidates/{email}")
	// @ApiOperation(value = "Finds Candidate By Id",
	public ResponseEntity<Candidate> getCandidateByID(@PathVariable("email") String email) {
		Candidate Candidate = candidateService.findCandidateByEmail(email);
		return new ResponseEntity<>(Candidate, HttpStatus.OK);
	}

	@PostMapping(value = "/candidate", consumes = "application/json")
	public ResponseEntity<Candidate> addCandidate(@RequestBody @Valid Candidate Candidate) {
		Candidate newCandidate = candidateService.addCandidate(Candidate);
		return new ResponseEntity<>(newCandidate, HttpStatus.CREATED);
	}

	@PutMapping("/candidate")
	public ResponseEntity<Candidate> updateCandidate(@RequestBody @Valid Candidate candidate) {
		Candidate updateCandidate = candidateService.updateCandidate(candidate);
		return new ResponseEntity<>(updateCandidate, HttpStatus.OK);
	}

	@DeleteMapping("/candidate/{email}")
	public ResponseEntity<?> deleteCandidate(@PathVariable("email") String candidate_id) {
		candidateService.deleteCandidate(candidate_id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/candidate/{candidate_id}/bookmark/{bookmark_id}")
	public ResponseEntity<Candidate> addBookmarkToCandidate(@PathVariable("candidate_id") String candidate_id, @PathVariable("bookmark_id") String bookmark_id ) {
		Candidate candidate = candidateService.addOrRemoveBookmarkToCandidate(candidate_id, bookmark_id);
		return new ResponseEntity<>(candidate, HttpStatus.CREATED);
	}
	@GetMapping("/candidates/tag/{tagId}")
	public List<Candidate> getCandidatesByTagId(@PathVariable("tagId") Long tagId) {
		
		return candidateService.getCandidatesByTagId(tagId);
		
	}
}
