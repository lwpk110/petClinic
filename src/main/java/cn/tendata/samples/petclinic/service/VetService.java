package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.Specialty;
import cn.tendata.samples.petclinic.data.jpa.domain.Vet;

public interface VetService extends EntityService<Vet, Integer> {

	Specialty saveSpecialty(Specialty vetSpecialty);

}
