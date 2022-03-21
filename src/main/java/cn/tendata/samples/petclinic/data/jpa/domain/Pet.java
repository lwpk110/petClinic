/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.tendata.samples.petclinic.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@pet_id")
@Entity
public class Pet extends NamedEntity<Integer> {
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDate birthDate;
	private PetType type;
	private Owner owner;
	private Set<Visit> visits = new LinkedHashSet<>();

	public Pet() {
	}

	public Pet(PetType type, String name) {
		this.type = type;
		this.setName(name);
	}

	public static Pet of(PetType petType, String name) {
		return new Pet(petType, name);
	}

	@Id
	@GeneratedValue
	@Override
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "birth_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@ManyToOne
	@JoinColumn(name = "type_id")
	public PetType getType() {
		return this.type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	@OneToMany(mappedBy = "pet",cascade = {CascadeType.MERGE,CascadeType.REMOVE, CascadeType.REFRESH},
		fetch = FetchType.EAGER,orphanRemoval = true)
	@OrderBy("visit_date ASC")
	public Set<Visit> getVisits() {
		return visits;
	}

	@ManyToOne
	@JoinColumn(name = "owner_id")
	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public void setVisits(Set<Visit> visits) {
		this.visits = visits;
	}

	public void addVisit(Visit visit) {
		getVisits().add(visit);
	}

}
