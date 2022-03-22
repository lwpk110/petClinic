package cn.tendata.samples.petclinic.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cn.tendata.samples.petclinic.controller.util.PetDataUtils;
import cn.tendata.samples.petclinic.controller.util.TestUtils;
import cn.tendata.samples.petclinic.data.jpa.domain.Pet;
import cn.tendata.samples.petclinic.data.jpa.domain.Visit;
import cn.tendata.samples.petclinic.data.jpa.repository.PetRepository;
import cn.tendata.samples.petclinic.data.jpa.repository.VisitRepository;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class PetControllerTest extends MockMvcTestSupport {

	@Autowired
	private VisitRepository visitRepository;

	@BeforeEach
	public void setup(RestDocumentationContextProvider restDocumentation) {
		super.setUp(restDocumentation);
	}


	@DataSet(value = {"owners.yml", "pets.yml", "pet_types.yml"})
	@Test
	void pagePet() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/pets").param("name", "美短"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.numberOfElements", Is.is(1)))
			.andExpect(jsonPath("$.content", Matchers.hasSize(1)))
			.andExpect(jsonPath("$.content[0].id", Is.is(2)))
			.andExpect(jsonPath("$.content[0].name", Is.is("美短1号")))
			.andReturn();
	}

	@DataSet(value = {"owners.yml", "pets.yml", "pet_types.yml"})
	@Test
	void getPet() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/pets/{id}", 3))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.id", Is.is(3)))
			.andExpect(jsonPath("$.name", Is.is("加菲猫1号")))
			.andReturn();
	}

	/**
	 * todo: date
	 *
	 * @throws Exception
	 */
	@DataSet(value = {"owners.yml", "pets.yml", "pet_types.yml"})
	@ExpectedDataSet(value = "expect/pet_update.yml", ignoreCols = "birth_date")  // test环境问题
	@Test
	void updatePet() throws Exception {

		Map<String, Object> updatePet = PetDataUtils.updatePetForm("泰迪1号_update", "2020-12-01");

		this.mockMvc.perform(put("/pets/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.convertObjectToJsonBytes(updatePet)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.id", Is.is(1)))
			.andExpect(jsonPath("$.name", Is.is("泰迪1号_update")))
			.andReturn();


	}

	/**
	 * todo: date
	 *
	 * @throws Exception
	 */
	@DataSet(value = {"owners.yml", "pets.yml", "pet_types.yml"})
	@ExpectedDataSet(value = "expect/pet_delete.yml", ignoreCols = "birth_date")
	@Test
	void deletePet() throws Exception {
		this.mockMvc.perform(delete("/pets/{id}", 1))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andReturn();
	}

	@DataSet(value = {"owners.yml", "pets.yml", "pet_types.yml", "vet.yml", "specialty.yml",
		"vet_specialties.yml"})
	@ExpectedDataSet(value = "expect/pet_addVisit.yml", ignoreCols = "visit_date")
	@Test
	void createVisit() throws Exception {

		Map<String, Object> petAddVisitForm = PetDataUtils.addVisitForm(1, "详情：牙齿蛀牙，用药：甲硝唑",
			"2022-02-24");

		this.mockMvc.perform(post("/pets/{id}/visits", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.convertObjectToJsonBytes(petAddVisitForm)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(jsonPath("$.date", Is.is("2022-02-24")))
			.andReturn();
	}

	/**
	 * todo: datebase rider 事物问题未解决
	 */
//	@Ignore
	@DataSet(value = {"owners.yml", "pets.yml", "pet_types.yml", "vet.yml", "specialty.yml",
		"vet_specialties.yml", "visit.yml"}, transactional = true, cleanBefore = true)
	@ExpectedDataSet(value = "expect/visit_update.yml", ignoreCols = "visit_date")
	@Test
	void updateVisit() throws Exception {

		Map<String, Object> updateVisitForm = new HashMap<>(2);
		updateVisitForm.put("description", "update 描述");

		this.mockMvc.perform(put("/pets/{id}/visits/{visitId}", 1, 1L)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(TestUtils.convertObjectToJsonBytes(updateVisitForm)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();

//		visitRepository.findById(1)
	}

	@DataSet(value = {"owners.yml", "pets.yml", "pet_types.yml", "vet.yml", "specialty.yml",
		"vet_specialties.yml", "visit.yml"}, transactional = true, cleanBefore = true)
	@Test
	void pagePetVisits() throws Exception {
		this.mockMvc.perform(get("/pets/{id}/visits", 1))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.content",Matchers.hasSize(1)))
			.andReturn();
	}
}
