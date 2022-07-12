package com.sndcorp.candidatemanage.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sndcorp.candidatemanage.entities.Candidate;
import com.sndcorp.candidatemanage.entities.Tag;
import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;
import com.sndcorp.candidatemanage.repo.CandidateRepository;
import com.sndcorp.candidatemanage.repo.TagRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagService {

	@Autowired
	private TagRepository tagRepo;

	@Autowired
	private CandidateRepository candidateRepo;

	public List<Tag> findAll() {
		List<Tag> tags = new ArrayList<Tag>();
		tagRepo.findAll().forEach(tags::add);
		return tags;
	}

	public Tag findById(Long id) {
		return tagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", id));
	}

	public boolean existsById(Long tagId) {
		return tagRepo.existsById(tagId);
	}

	public List<Candidate> findCandidatesByTagsId(Long tagId) {
		List<Candidate> candidates = candidateRepo.findCandidatesByTagsId(tagId);
		return candidates;
	}

	public void deleteById(long id) {
		tagRepo.deleteById(id);
	}

	public Tag addTag(String candidateId, Tag tagRequest) {
		Candidate candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId));

		Optional<Tag> opt_tag = tagRepo.findByName(tagRequest.getName());

		if (opt_tag.isPresent()) {
			log.debug("Tag is already present hence linking it to candidate...");
			candidate.addTag(opt_tag.get());
			candidateRepo.save(candidate);
			return opt_tag.get();
		} else {

			log.debug("Tag is Not present hence Creating Tag, linking it to candidate...");
			candidate.addTag(tagRequest);
			candidateRepo.save(candidate);
			return tagRequest;
		}
	}

	public void deleteTagFromCandidate(String candidateId, Long tagId) {
		Candidate candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId));

		candidate.removeTag(tagId);
		candidateRepo.save(candidate);

	}

}
