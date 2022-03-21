package cn.tendata.samples.petclinic.service;

import cn.tendata.samples.petclinic.data.jpa.domain.Owner;
import cn.tendata.samples.petclinic.data.jpa.repository.OwnerRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl extends
        EntityServiceSupport<Owner, Integer, OwnerRepository> implements OwnerService {

    protected OwnerServiceImpl(
            OwnerRepository repository) {
        super(repository);
    }

	@Override
	public Page<Owner> getAll(Owner query, Pageable pageable) {
		ExampleMatcher matcher = ExampleMatcher.matching()
			.withStringMatcher(StringMatcher.CONTAINING)
			.withIgnoreNullValues();
		Example<Owner> example = Example.of(query, matcher);
		return getRepository().findAll(example,pageable);
	}
}
