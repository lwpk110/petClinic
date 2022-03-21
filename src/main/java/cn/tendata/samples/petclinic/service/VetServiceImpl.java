package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.Specialty;
import cn.tendata.samples.petclinic.data.jpa.domain.Vet;
import cn.tendata.samples.petclinic.data.jpa.repository.VetRepository;
import org.springframework.stereotype.Service;

@Service
public class VetServiceImpl extends EntityServiceSupport<Vet, Integer, VetRepository> implements
	VetService {

	protected VetServiceImpl(
		VetRepository repository) {
		super(repository);
	}

	@Override
	public Specialty saveSpecialty(
		Specialty vetSpecialty) {
		return null;
	}
}
