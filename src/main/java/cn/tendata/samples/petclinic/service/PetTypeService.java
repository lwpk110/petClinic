package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetTypeService extends EntityService<PetType,Integer> {

    Page<PetType> getAll(Pageable pageable);
}
