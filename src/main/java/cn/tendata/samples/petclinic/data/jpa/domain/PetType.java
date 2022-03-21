package cn.tendata.samples.petclinic.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PetType extends NamedEntity<Integer> {


	public PetType() {
	}

	public PetType(int id) {
		this.setId(id);
	}

	@Id
	@GeneratedValue
	@Override
	public Integer getId() {
		return super.getId();
	}
}
