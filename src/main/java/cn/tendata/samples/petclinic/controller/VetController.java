package cn.tendata.samples.petclinic.controller;


import cn.tendata.samples.petclinic.bind.annotation.RequestBodyPathVariable;
import cn.tendata.samples.petclinic.controller.util.WebUtils;
import cn.tendata.samples.petclinic.data.jpa.domain.PetType;
import cn.tendata.samples.petclinic.data.jpa.domain.Vet;
import cn.tendata.samples.petclinic.service.PetService;
import cn.tendata.samples.petclinic.service.VetService;
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
@RequestMapping("/vets")
public class VetController {

	private final VetService service;

	@Autowired
	public VetController(VetService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<Vet>> pageVets(@PageableDefault Pageable pageable) {
		return WebUtils.pageResponse(Page.empty());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Vet> getVet(@PathVariable Vet vet) {
		return WebUtils.fetchResponse(vet);
	}

	@PostMapping
	public ResponseEntity<Vet> createVet(@RequestBody @Validated Vet vet) {
		service.save(vet);
		return WebUtils.createdResponse(vet);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Vet> updateVet(
		@Validated @RequestBodyPathVariable("id") Vet vet) {
		service.save(vet);
		return WebUtils.updateResponse(vet);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVet(@PathVariable("id") Vet vet) {
		service.delete(vet);
		return WebUtils.deletedResponse();
	}

}
