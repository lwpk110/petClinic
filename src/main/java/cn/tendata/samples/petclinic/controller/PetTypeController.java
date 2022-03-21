package cn.tendata.samples.petclinic.controller;


import cn.tendata.samples.petclinic.bind.annotation.RequestBodyPathVariable;
import cn.tendata.samples.petclinic.controller.util.WebUtils;
import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import cn.tendata.samples.petclinic.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet-types")
public class PetTypeController {

	private final PetTypeService service;

	@Autowired
	public PetTypeController(PetTypeService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<PetType>> pagePetType(@PageableDefault Pageable pageable) {
		Page<PetType> page = this.service.getAll(pageable);
		return WebUtils.pageResponse(page);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PetType> getPetType(@PathVariable("id") PetType petType) {
		return WebUtils.fetchResponse(petType);
	}

	@PostMapping
	public ResponseEntity<PetType> createPetType(@RequestBody @Validated PetType petType) {
		service.save(petType);
		return WebUtils.createdResponse(petType);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PetType> updatePetType(
		@Validated @RequestBodyPathVariable("id") PetType petType) {
		service.save(petType);
		return WebUtils.updateResponse(petType);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePetType(@PathVariable("id") PetType petType) {
		service.delete(petType);
		return WebUtils.deletedResponse();
	}
}
