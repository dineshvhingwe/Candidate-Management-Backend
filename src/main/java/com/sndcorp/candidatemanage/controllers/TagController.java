package com.sndcorp.candidatemanage.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Tag;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.services.TagService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TagController {

	@Autowired
	private TagService tagService;

	@GetMapping("/tags")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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

	@GetMapping("/tag/{id}/candidates")
	public ResponseEntity<List<Candidate>> getAllCandidatesByTagId(@PathVariable(value = "id") Long id) {
		if (!tagService.existsById(id)) {
			throw new ResourceNotFoundException("Tag", id);
		}
		return new ResponseEntity<>(tagService.findCandidatesByTags(id), HttpStatus.OK);
	}

	@PostMapping("/candidate/{candidateId}/tag")
	public ResponseEntity<Tag> addTag(@PathVariable(value = "candidateId") String candidateId,
			@RequestBody Tag tagRequest) {
		Tag tag = tagService.addTag(candidateId, tagRequest);
		return new ResponseEntity<Tag>(tag, HttpStatus.CREATED);
	}

	@DeleteMapping("/candidate/{candidateId}/tag/{tag_id}")
	public ResponseEntity<HttpStatus> deleteTagFromCandidate(@PathVariable(value = "candidateId") String candidateId,
			@PathVariable(value = "tag_id") Long tag_id) {
		
		tagService.deleteTagFromCandidate(candidateId, tag_id);
		log.debug("deleteTagFromCandidate successfully deleted tag {}", tag_id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tag/{tag_id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<HttpStatus> deleteTag(@PathVariable("tag_id") Long tag_id) {
		tagService.deleteById(tag_id);
		log.debug("deleteTag successfully deleted tag {}", tag_id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}