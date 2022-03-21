package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import cn.tendata.samples.petclinic.data.jpa.repository.PetTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PetTypeServiceImpl extends
	EntityServiceSupport<PetType, Integer, PetTypeRepository> implements
	PetTypeService {

	protected PetTypeServiceImpl(
		PetTypeRepository repository) {
		super(repository);
	}

	@Override
	public Page<PetType> getAll(
		Pageable pageable) {
		return getRepository().findAll(pageable);
	}
}
