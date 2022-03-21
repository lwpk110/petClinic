package cn.tendata.samples.petclinic.controller;


import cn.tendata.samples.petclinic.bind.annotation.RequestBodyPathVariable;
import cn.tendata.samples.petclinic.controller.dto.VisitUpdateDto;
import cn.tendata.samples.petclinic.controller.util.WebUtils;
import cn.tendata.samples.petclinic.data.jpa.domain.Pet;
import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import cn.tendata.samples.petclinic.data.jpa.domain.Visit;
import cn.tendata.samples.petclinic.service.PetService;
import java.util.ArrayList;
import java.util.Set;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pets")
public class PetController {

	private final PetService service;

	@Autowired
	public PetController(PetService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<Pet>> pagePet(
		@RequestParam(required = false, name = "typeId") PetType petType,
		@RequestParam(required = false) String name,
		@PageableDefault Pageable pageable) {
		Page<Pet> page = service.getAll(Pet.of(petType, name), pageable);
		return WebUtils.pageResponse(page);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pet> getPet(@PathVariable("id") Pet pet) {
		return WebUtils.fetchResponse(pet);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pet> updatePet(@Validated @RequestBodyPathVariable("id") Pet pet) {
		service.save(pet);
		return WebUtils.updateResponse(pet);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePet(@PathVariable("id") Pet pet) {
		service.delete(pet);
		return WebUtils.deletedResponse();
	}

	/*
	 *pet visit
	 */
	@PostMapping("/{id}/visits")
	public ResponseEntity<Visit> createVisit(
		@PathVariable("id") Pet pet,
		@Validated @RequestBody Visit visit){
		visit.setPet(pet);
		pet.addVisit(visit);
		this.service.save(pet);
		return new ResponseEntity<>(visit, HttpStatus.CREATED);
	}


	@PutMapping("/{id}/visits/{visitId}")
	@Transactional
	public ResponseEntity<Visit> updateVisit(
		@PathVariable("id") Pet pet,
		@PathVariable("visitId") Visit visit,
		@RequestBody VisitUpdateDto dto){
		visit.setDescription(dto.getDescription());
		pet.addVisit(visit);
		this.service.save(pet);
		return WebUtils.updateResponse(visit);
	}

	@GetMapping("/{id}/visits")
	public ResponseEntity<Page<Visit>> pagePetVisits(
		@PathVariable("id") Pet pet,
		@PageableDefault Pageable page) {
		Set<Visit> visits = pet.getVisits();
		return WebUtils.pageResponse(new PageImpl<>(new ArrayList<>(visits)));
	}
}
