package cn.tendata.samples.petclinic.controller.util;

import cn.tendata.samples.petclinic.data.jpa.domain.Vet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PetDataUtils {

	public static Map<String,Object> createPet(int petTypeId, String petName, String birthDate){
//		Pet pet = new Pet(new PetType(petTypeId), petName);
//		pet.setBirthDate(birthDate);

		Map<String,Object> createForm = new HashMap<>(2);
		createForm.put("name", petName);
		createForm.put("birthDate",birthDate);
		createForm.put("type",Collections.singletonMap("id",2));
		return createForm;

	}

	public static Map<String, Object> updatePetForm(String petName, String birthDateStr){
		Map<String,Object> updateForm = new HashMap<>(2);
		updateForm.put("name", petName);
		updateForm.put("birthDate",birthDateStr);
		return updateForm;
	}

	public static Map<String,Object> addVisitForm(int vetId,  String description, String visitDate){
		Map<String,Object> updateForm = new HashMap<>(2);
		updateForm.put("date", visitDate);
		updateForm.put("vet",Vet.ofId(vetId));
		updateForm.put("description", description);
		return updateForm;
	}

}
