package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OwnerService extends EntityService<Owner,Integer> {

	Page<Owner> getAll(Owner query, Pageable pageable);
}
