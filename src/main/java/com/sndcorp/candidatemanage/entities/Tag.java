package com.sndcorp.candidatemanage.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TAGS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Serializable{

	private static final long serialVersionUID = -7582493920779267168L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String name;

	/*
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "candidates_tags", 
	joinColumns = @JoinColumn(name = "tag_id"), 
	inverseJoinColumns = @JoinColumn(name = "candidate_id") )
	@JsonIgnore
	private List<Candidate> candidates; */
	
	@ManyToMany(mappedBy = "tags")
	@JsonIgnore
	private List<Candidate> candidates;

	@Override
	public String toString() {
		return "Tag [name=" + name + "]";
	}
}