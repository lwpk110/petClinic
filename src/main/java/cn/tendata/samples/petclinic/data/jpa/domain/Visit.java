
package cn.tendata.samples.petclinic.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@visit_id")
@Entity
public class Visit extends AbstractEntity<Long> {

	@NotNull
	private LocalDate date;
	@NotEmpty
	private String description;
	private Pet pet;
	private Vet vet;

	public Visit() {
		this.date = LocalDate.now();
	}

	@Id
	@GeneratedValue
	@Override
	public Long getId() {
		return super.getId();
	}

	@Column(name = "visit_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "pet_id")
	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	@ManyToOne
	@JoinColumn(name = "vet_id")
	public Vet getVet() {
		return vet;
	}

	public void setVet(Vet vet) {
		this.vet = vet;
	}
}
