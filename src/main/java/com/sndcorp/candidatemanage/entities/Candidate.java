package com.sndcorp.candidatemanage.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.sndcorp.candidatemanage.exceptions.ResourceNotFoundException;

import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = {"bookmarkedCandidates"})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Candidates")
public class Candidate implements Serializable {

	private static final long serialVersionUID = 17413739818370321L;

	@Id // primary key
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(length = 36)
	private String candidateId;

	private String name;

	private String surname;

	// @Lob
	// private String about;
	@Size(min = 5, max = 20)
	@Column(unique = true)
	private String username;

	@Column(unique = true, updatable = false)
	@Email
	private String email;

	@Column(unique = true, nullable = false)
	@Pattern(regexp = "(^$|[0-9]{10})")
	private String phone;

	private Gender gender;

	private String imageUrl;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	// extra column in Candidate table address_id as FK to addree_ID in Address
	@JoinColumn(name = "address_id")
	private Address address;

	
	 @ElementCollection(targetClass = String.class)
	 @Type(type = "org.hibernate.type.StringType")
	 @JsonIgnore 
	 private Set<String> bookmarkedCandidates; 

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "candidates_tags", joinColumns = @JoinColumn(name = "candidate_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tags = new HashSet<>();

	@Column(name = "date_created")
	@CreationTimestamp
	private Date dateCreated;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdated;

	// getters and setters to easeout adding/removings tags to collection
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	public void removeTag(Long tag_id) {
		Tag tag = this.tags.stream().filter(t -> t.getId() == tag_id).findFirst().orElse(null);
		if (tag != null) {
			this.tags.remove(tag);
		} else {
			throw new ResourceNotFoundException("Tag", tag_id);
		}
	}
	
	// getters and setters to easeout adding/removings bookmarks to collections
	public void addBookmark(String bookmark) {
		this.bookmarkedCandidates.add(bookmark);
	}

	public void removeBookmark(String bookmark) {
		if (!Collections.isEmpty(bookmarkedCandidates) && bookmarkedCandidates.contains(bookmark)) {
			this.bookmarkedCandidates.remove(bookmark);
		} else {
			throw new ResourceNotFoundException("Bookmark", bookmark);
		}
	}
}
