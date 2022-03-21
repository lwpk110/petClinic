package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.Specialty;
import cn.tendata.samples.petclinic.data.jpa.repository.SpecialtyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyServiceImpl extends
	EntityServiceSupport<Specialty, Integer, SpecialtyRepository> implements
	SpecialtyService {

	protected SpecialtyServiceImpl(
		SpecialtyRepository repository) {
		super(repository);
	}

	@Override
	public Page<Specialty> getAll(
		Pageable pageable) {
		return getRepository().findAll(pageable);
	}
}
