package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.Pet;
import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetService extends EntityService<Pet,Integer> {

	Page<Pet> getAll(Pet query, Pageable pageable);



	PetType savePetType(PetType petType);

}
