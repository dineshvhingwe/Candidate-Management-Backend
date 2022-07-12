package com.sndcorp.candidatemanage.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
	public ResponseEntity<?> deleteCandidate(@PathVariable("email") String email) {
		candidateService.deleteCandidate(email);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
		log.error("MethodArgumentNotValidException Occurred :{}", ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> dataIntegrityViolationExceptionExceptionHandler(DataIntegrityViolationException ex) {
		log.error("Non Rollable DataIntegrityViolationException Occurred :{}", ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> exceptionHandler(RuntimeException ex) {
		log.error("Non Rollable RuntimeException Occurred :{}", ex);
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
