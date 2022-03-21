package cn.tendata.samples.petclinic.controller;


import cn.tendata.samples.petclinic.bind.annotation.RequestBodyPathVariable;
import cn.tendata.samples.petclinic.controller.util.WebUtils;
import cn.tendata.samples.petclinic.data.jpa.domain.Owner;
import cn.tendata.samples.petclinic.data.jpa.domain.Pet;
import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import cn.tendata.samples.petclinic.service.OwnerService;
import cn.tendata.samples.petclinic.service.PetService;
import cn.tendata.samples.petclinic.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/owners")
public class OwnerController {

	private final OwnerService service;
	private final PetTypeService petTypeService;
	private final PetService petService;

	@Autowired
	public OwnerController(OwnerService service,
		PetTypeService petTypeService, PetService petService) {
		this.service = service;
		this.petTypeService = petTypeService;
		this.petService = petService;
	}

	@GetMapping
	public ResponseEntity<Page<Owner>> pageOwner(
		@RequestParam(required = false) String firstName,
		@RequestParam(required = false) String lastName,
		@PageableDefault Pageable page) {
		return WebUtils.pageResponse(this.service.getAll(Owner.of(firstName,lastName),page));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Owner> getOwner(@PathVariable("id") Owner owner) {
		return WebUtils.fetchResponse(owner);
	}

	@PostMapping
	public ResponseEntity<Owner> createOwner(@RequestBody @Validated Owner owner) {
		service.save(owner);
		return WebUtils.createdResponse(owner);
	}

	/**
	 *  update 如果局部更新不建议使用 {@link Validated}注解
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Owner> updateOwner(
		 @RequestBodyPathVariable("id") Owner owner) {
		service.save(owner);
		return WebUtils.updateResponse(owner);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOwner(@PathVariable("id") Owner owner) {
		service.delete(owner);
		return WebUtils.deletedResponse();
	}

	@PostMapping("/{ownerId}/pets")
	public ResponseEntity<Pet> addPet(@PathVariable("ownerId") Owner owner,
		@Validated @RequestBody Pet pet) {
		pet.setOwner(owner);
		Pet save = petService.save(pet);
		return new ResponseEntity<>(save, HttpStatus.CREATED);
	}
}
