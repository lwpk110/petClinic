package cn.tendata.samples.petclinic.data.jpa.repository;

import cn.tendata.samples.petclinic.data.jpa.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Integer> {

}
