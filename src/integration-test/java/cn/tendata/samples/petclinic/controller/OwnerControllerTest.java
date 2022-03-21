package cn.tendata.samples.petclinic.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cn.tendata.samples.petclinic.controller.util.OwnerDataUtils;
import cn.tendata.samples.petclinic.controller.util.PetDataUtils;
import cn.tendata.samples.petclinic.controller.util.TestUtils;
import cn.tendata.samples.petclinic.data.jpa.domain.Owner;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import java.util.Map;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class OwnerControllerTest extends MockMvcTestSupport {

	@BeforeEach
	public void setup(RestDocumentationContextProvider restDocumentation) {
		super.setUp(restDocumentation);
	}

	@DataSet(value = {"owners.yml"}, cleanBefore = true)
	@Test
	void pageOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "元"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.numberOfElements", Is.is(1)))
			.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
			.andExpect(jsonPath("$.content[0].id", Is.is(2)))
			.andExpect(jsonPath("$.content[0].lastName", Is.is("元霸")))
			.andReturn();
	}

	@DataSet(value = {"owners.yml"})
	@Test
	void getOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{id}", 1))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.id", Is.is(1)))
			.andExpect(jsonPath("$.lastName", Is.is("翠山")))
			.andReturn();
	}

	@DataSet(value = {"owners.yml"})
	@Test
	void getOwner_idNotExist_return500() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{id}", 99))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isInternalServerError())
			.andReturn();
	}

	@DataSet(value = {"owners.yml"})
	@ExpectedDataSet(value = {"expect/owner_create.yml"})
	@Test
	void createOwner() throws Exception {
		Owner createRequest = OwnerDataUtils.createOwner("王", "麻子", "99999");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.convertObjectToJsonBytes(createRequest)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();
	}

	@DataSet(value = {"owners.yml"})
	@Test
	void createOwner_telIsBlank_return400() throws Exception {
		Owner createRequest = OwnerDataUtils.createOwner("王", "麻子", "");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.convertObjectToJsonBytes(createRequest)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andReturn();
	}

	@DataSet(value = {"owners.yml"})
	@ExpectedDataSet(value = {"expect/owner_update.yml"})
	@Test
	void updateOwner() throws Exception {

		Owner updateDto = OwnerDataUtils.createOwner("赵", "六", null);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/owners/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.convertObjectToJsonBytes(updateDto)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
	}

	@DataSet(value = {"owners.yml"}, transactional = true, cleanBefore = true)
	@ExpectedDataSet(value = {"expect/owner_delete.yml"})
	@Test
	void deleteOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/owners/{id}", 2))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andReturn();
	}

	/**
	 * todo: date
	 */
	@DataSet(value = {"owners.yml","pets.yml","pet_types.yml"}, transactional = true, cleanBefore = true)
	@ExpectedDataSet(value = {"expect/owner_addPet.yml"},ignoreCols = {"birth_date","type_id"})
	@Test
	 void addPet() throws Exception {

		Map<String, Object> addPetRequest = PetDataUtils.createPet(2, "加菲猫2号", null);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{id}/pets", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.convertObjectToJsonBytes(addPetRequest)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();
	}
}
