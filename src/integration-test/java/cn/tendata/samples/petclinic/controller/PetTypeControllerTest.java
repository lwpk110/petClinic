package cn.tendata.samples.petclinic.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.github.database.rider.core.api.dataset.DataSet;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class PetTypeControllerTest extends MockMvcTestSupport {

	@DataSet("pet_types.yml")
	@Test
	void pagePetType() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pet-types"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.numberOfElements", Is.is(2)))
			.andExpect(jsonPath("$.content", Matchers.hasSize(2)))
			.andReturn();
	}

	@Test
	void getPetType() {
	}

	@Test
	void createPetType() {
	}

	@Test
	void updatePetType() {
	}

	@Test
	void deletePetType() {
	}
}
