package cn.tendata.samples.petclinic.data.jpa.repository;

import cn.tendata.samples.petclinic.data.jpa.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {

}
