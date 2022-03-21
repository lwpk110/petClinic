package cn.tendata.samples.petclinic.controller.util;

import com.github.database.rider.core.replacers.Replacer;
import org.dbunit.dataset.ReplacementDataSet;

public class LocalDateReplacer implements Replacer {

	@Override
	public void addReplacements(ReplacementDataSet dataSet) {
		System.out.println(dataSet);
	}
}
