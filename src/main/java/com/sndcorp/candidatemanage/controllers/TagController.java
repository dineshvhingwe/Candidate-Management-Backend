package com.sndcorp.candidatemanage.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Tag;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.repo.CandidateRepository;
import com.sndcorp.candidatemanage.services.TagService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TagController {

	@Autowired
	private TagService tagService;

	@GetMapping("/tags")
	public ResponseEntity<List<Tag>> getAllTags() {
		List<Tag> tags = tagService.findAll();
		if (tags.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping("/candidate/{candidateId}/tags")
	public ResponseEntity<List<Tag>> getAllTagsByCandidateId(@PathVariable(value = "candidateId") String candidateId) {
		List<Tag> tags = tagService.findTagsByCandidate(candidateId);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping("/tags/{id}")
	public ResponseEntity<Tag> getTagsById(@PathVariable(value = "id") Long id) {
		Tag tag = tagService.findById(id);
		return new ResponseEntity<>(tag, HttpStatus.OK);
	}

	@GetMapping("/tags/{tagId}/candidates")
	public ResponseEntity<List<Candidate>> getAllCandidatesByTagId(@PathVariable(value = "tagId") Long tagId) {
		if (!tagService.existsById(tagId)) {
			throw new ResourceNotFoundException("Tag", tagId);
		}
		return new ResponseEntity<>(tagService.findCandidatesByTagsId(tagId), HttpStatus.OK);
	}

	@PostMapping("/candidates/{candidateId}/tags")
	public ResponseEntity<Tag> addTag(@PathVariable(value = "candidateId") String candidateId,
			@RequestBody Tag tagRequest) {
		Tag tag = tagService.addTag(candidateId, tagRequest);
		return new ResponseEntity<Tag>(tag, HttpStatus.CREATED);
	}

	@DeleteMapping("/candidates/{candidateId}/tags/{tagId}")
	public ResponseEntity<HttpStatus> deleteTagFromCandidate(@PathVariable(value = "candidateId") String candidateId,
			@PathVariable(value = "tagId") Long tagId) {
		tagService.deleteTagFromCandidate(candidateId, tagId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tags/{id}")
	public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") long id) {
		tagService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}