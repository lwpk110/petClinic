package cn.tendata.samples.petclinic.controller;

import cn.tendata.samples.petclinic.bind.annotation.RequestBodyPathVariable;
import cn.tendata.samples.petclinic.controller.util.WebUtils;
import cn.tendata.samples.petclinic.data.jpa.domain.Specialty;
import cn.tendata.samples.petclinic.service.SpecialtyService;
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
@RequestMapping("/vet-specialties")
public class VetSpecialtyController {

	private final SpecialtyService service;

	@Autowired
	public VetSpecialtyController(SpecialtyService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<Page<Specialty>> pageSpecialties(@PageableDefault Pageable pageable) {
		Page<Specialty> page = service.getAll(pageable);
		return WebUtils.pageResponse(page);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Specialty> getSpecialty(@PathVariable("id") Specialty vetSpecialty) {
		return WebUtils.fetchResponse(vetSpecialty);
	}

	@PostMapping
	public ResponseEntity<Specialty> createSpecialty(@RequestBody @Validated Specialty vetSpecialty) {
		service.save(vetSpecialty);
		return WebUtils.createdResponse(vetSpecialty);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Specialty> updateSpecialty(
		@Validated @RequestBodyPathVariable("id") Specialty vetSpecialty) {
		service.save(vetSpecialty);
		return WebUtils.updateResponse(vetSpecialty);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSpecialty(@PathVariable("id") Specialty vetSpecialty) {
		service.delete(vetSpecialty);
		return WebUtils.deletedResponse();
	}
}
