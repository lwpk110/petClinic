package cn.tendata.samples.petclinic.data.jpa.repository;

import cn.tendata.samples.petclinic.data.jpa.domain.Owner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

}
