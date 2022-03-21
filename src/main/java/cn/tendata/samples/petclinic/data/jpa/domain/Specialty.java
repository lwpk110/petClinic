package cn.tendata.samples.petclinic.data.jpa.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Specialty extends NamedEntity<Integer> {

	private Set<Vet> vets;


	@Id
	@GeneratedValue
	@Override
	public Integer getId() {
		return super.getId();
	}

	@ManyToMany(mappedBy ="specialties" )
	public Set<Vet> getVets() {
		return vets;
	}

	public void setVets(Set<Vet> vets) {
		this.vets = vets;
	}
}
