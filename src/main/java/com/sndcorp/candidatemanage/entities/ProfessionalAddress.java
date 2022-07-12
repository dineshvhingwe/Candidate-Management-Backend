package com.sndcorp.candidatemanage.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "ProfessionalAddresses")
public class ProfessionalAddress implements Serializable{

	private static final long serialVersionUID = -4554720802154573L;

	@Id // primary key
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@JsonIgnore
	@Column(length = 36)
	private String address_id;

	@Max(999999) @Min(100000)
	private int pin;

	private String city;

	private String town;
	
	@Length (min = 8, max=50)
	private String addressline;
	
}
