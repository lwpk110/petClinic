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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

@Where(clause = "deleted=false")
@SQLDelete(sql = "update owners set deleted=true where id =?")
@Entity
public class Owner extends Person<Integer> {

	private String address;
	private String city;
	private String telephone;
	private Boolean deleted;
	private List<Pet> pets = new ArrayList<>();

	public Owner(String fn, String ln, String telephone) {
		this.setFirstName(fn);
		this.setLastName(ln);
		this.telephone = telephone;
		this.deleted = false;
	}

	public Owner() {
		this.deleted =false;
	}

	@Id
	@GeneratedValue
	@Override
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "city",length = 15)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "telephone")
	@NotEmpty
	@Digits(fraction = 0, integer = 11)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@OneToMany(mappedBy = "owner",fetch = FetchType.EAGER, orphanRemoval = true, cascade ={CascadeType.PERSIST} )
	@OrderBy("name")
	@JsonIgnore
	public List<Pet> getPets() {
		return this.pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void addPet(Pet pet) {
		if (pet.isNew()) {
			getPets().add(pet);
		}
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Owner.
	 *
	 * @param name to test
	 * @return true if pet name is already in use
	 */
	public Pet getPet(String name) {
		return getPet(name, false);
	}

	/**
	 * Return the Pet with the given id, or null if none found for this Owner.
	 *
	 * @return a pet if pet id is already in use
	 */
	public Pet getPet(Integer id) {
		for (Pet pet : getPets()) {
			if (!pet.isNew()) {
				Integer compId = pet.getId();
				if (compId.equals(id)) {
					return pet;
				}
			}
		}
		return null;
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Owner.
	 *
	 * @param name to test
	 * @return true if pet name is already in use
	 */
	public Pet getPet(String name, boolean ignoreNew) {
		name = name.toLowerCase();
		for (Pet pet : getPets()) {
			if (!ignoreNew || !pet.isNew()) {
				String compName = pet.getName();
				compName = compName == null ? "" : compName.toLowerCase();
				if (compName.equals(name)) {
					return pet;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew())
			.append("lastName", this.getLastName()).append("firstName", this.getFirstName())
			.append("address", this.address).append("city", this.city)
			.append("telephone", this.telephone)
			.toString();
	}

	/**
	 * Adds the given {@link Visit} to the {@link Pet} with the given identifier.
	 *
	 * @param petId the identifier of the {@link Pet}, must not be {@literal null}.
	 * @param visit the visit to add, must not be {@literal null}.
	 */
	public Owner addVisit(Integer petId, Visit visit) {

		Assert.notNull(petId, "Pet identifier must not be null!");
		Assert.notNull(visit, "Visit must not be null!");

		Pet pet = getPet(petId);

		Assert.notNull(pet, "Invalid Pet identifier!");

		pet.addVisit(visit);

		return this;
	}

	public static Owner of(String firstName, String lastName){
		Owner owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		return owner;
	}

}
