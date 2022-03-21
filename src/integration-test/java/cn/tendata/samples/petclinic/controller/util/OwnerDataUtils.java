package cn.tendata.samples.petclinic.controller.util;

import cn.tendata.samples.petclinic.data.jpa.domain.Owner;

public class OwnerDataUtils {

	public static Owner createOwner(String firstName, String lastName, String tel){
		return new Owner(firstName,lastName, tel);
	}

}
