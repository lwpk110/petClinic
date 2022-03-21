package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import cn.tendata.samples.petclinic.data.jpa.domain.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpecialtyService extends EntityService<Specialty,Integer> {

    Page<Specialty> getAll(Pageable pageable);

}
