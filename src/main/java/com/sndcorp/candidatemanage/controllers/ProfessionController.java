package com.sndcorp.candidatemanage.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sndcorp.candidatemanage.entities.Profession;
import com.sndcorp.candidatemanage.services.ProfessionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ProfessionController {

	@Autowired
	private ProfessionService professionService;

	@GetMapping("/professions")
	public ResponseEntity<List<Profession>> getAllProfessions() {
		List<Profession> Professions = professionService.findAllProfessions();
		return new ResponseEntity<>(Professions, HttpStatus.OK);
	}

	@GetMapping("/candidate/{candidate_id}/professions")
	public ResponseEntity<List<Profession>> getProfessionsByCandidate(
			@PathVariable("candidate_id") String candidate_id) {
		List<Profession> professions = professionService.findProfessionByCandidate(candidate_id);
		return new ResponseEntity<>(professions, HttpStatus.OK);
	}

	@PostMapping(value = "/candidate/{candidate_id}/profession", consumes = "application/json")
	public ResponseEntity<Profession> addProfessionToCandidate(@PathVariable("candidate_id") String candidate_id,
			@RequestBody Profession profession) {

		Profession newprofession = professionService.addProfessionToCandidate(candidate_id, profession);
		return new ResponseEntity<>(newprofession, HttpStatus.CREATED);
	}

	@DeleteMapping("/professions/{profession_id}")
	public ResponseEntity<?> deleteProfession(@PathVariable("profession_id") String id) {
		professionService.deleteProfession(id);
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
