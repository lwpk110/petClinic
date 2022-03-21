package cn.tendata.samples.petclinic.data.jpa.repository;

import cn.tendata.samples.petclinic.data.jpa.domain.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {

}
