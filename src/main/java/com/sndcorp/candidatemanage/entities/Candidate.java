package com.sndcorp.candidatemanage.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@ToString
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
	private String candidate_id;

	private String name;
	private String surname;

	// @Lob
	// private String about;

	@Column(unique = true, updatable = false)
	@Email
	private String email;

	@Column(unique = true, nullable = false)
	@Pattern(regexp = "(^$|[0-9]{10})")
	private String phone;

	private Gender gender;

	private String imageUrl;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	// extra column in Candidate table address_id as FK to addree_ID in Address PK
	private Address address;

	@ElementCollection(targetClass = UUID.class)
	// @Column(columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private Set<UUID> bookmarkedCandidates = new TreeSet<UUID>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "candidates_tags", 
	joinColumns = @JoinColumn(name = "candidate_id"), 
	inverseJoinColumns = @JoinColumn(name = "tag_id") )
	private Set<Tag> tags = new HashSet<>();

	@Column(name = "date_created")
	@CreationTimestamp
	private Date dateCreated;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdated;

	// getters and setters
	public void addTag(Tag tag) {
		this.tags.add(tag);
		//tag.getCandidates().add(this);
	}

	public void removeTag(long tagId) {
		Tag tag = this.tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
		if (tag != null) {
			this.tags.remove(tag);
			//tag.getCandidates().remove(this);
		}
	}
}
