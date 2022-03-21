package cn.tendata.samples.petclinic.data.jpa.repository;

import cn.tendata.samples.petclinic.data.jpa.domain.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Integer> {

}
