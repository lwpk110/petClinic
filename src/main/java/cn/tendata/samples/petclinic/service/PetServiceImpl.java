package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.Pet;
import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import cn.tendata.samples.petclinic.data.jpa.repository.PetRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl extends EntityServiceSupport<Pet, Integer, PetRepository> implements
        PetService {

    protected PetServiceImpl(
            PetRepository repository) {
        super(repository);
    }

	@Override
	public Page<Pet> getAll(Pet query, Pageable pageable) {
		ExampleMatcher matcher = ExampleMatcher.matching()
			.withIgnoreNullValues()
			.withStringMatcher(StringMatcher.CONTAINING);
		Example<Pet> example = Example.of(query, matcher);
		return getRepository().findAll(example, pageable);
	}

	@Override
    public PetType savePetType(
            PetType petType) {
        return null;
    }
}
