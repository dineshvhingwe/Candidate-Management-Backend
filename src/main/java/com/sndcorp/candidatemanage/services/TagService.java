package com.sndcorp.candidatemanage.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
		log.debug("found all tags: {}" , tags);
		return tags;
	}

	public Tag findById(Long id) {
		return tagRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", id));
	}

	public boolean existsById(Long id) {
		return tagRepo.existsById(id);
	}

	public List<Candidate> findCandidatesByTags(Long tag_Id) {
		Tag tag = tagRepo.findById(tag_Id).orElseThrow(()-> new ResourceNotFoundException("Tag", tag_Id));
				
		List<Candidate> candidates = candidateRepo.getCandidatesByTags(tag);
		log.debug("found Candidate by Tag {} ARE:: {}", tag_Id, candidates);
		return candidates;
	}

	public void deleteById(Long tag_id) {
		log.debug("deleting tag {}", tag_id);
		tagRepo.deleteById(tag_id);
	}

	public Tag addTag(String candidateId, Tag tagRequest) {
		Candidate candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId));
		log.debug("adding tag to candidate : {}", candidate.getUsername());
		tagRequest.setName(tagRequest.getName().toLowerCase()); //store all tags in lowercase
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

	public void deleteTagFromCandidate(String candidateId, Long tag_id) {
		Candidate candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId));

		candidate.removeTag(tag_id);
		log.debug("removed tag from candidate {}. Persisting the Candidate...", candidate);
		candidateRepo.save(candidate);
	}

	public List<Tag> findTagsByCandidate(String candidateId) {
		Candidate candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId));

		List<Tag> tags = tagRepo.findTagsByCandidates(candidate, Sort.by("name").descending());
		tags.forEach(tag -> log.debug("findTagsByCandidate tag name: {}", tag.getName()));
		return tags;
	}

}
