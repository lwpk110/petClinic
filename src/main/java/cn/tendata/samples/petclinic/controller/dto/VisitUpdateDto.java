package cn.tendata.samples.petclinic.controller.dto;

import javax.validation.constraints.NotBlank;

public class VisitUpdateDto {

	@NotBlank
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
