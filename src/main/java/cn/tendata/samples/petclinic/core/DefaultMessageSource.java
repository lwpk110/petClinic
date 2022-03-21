package cn.tendata.samples.petclinic.core;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class DefaultMessageSource extends ResourceBundleMessageSource {

	public DefaultMessageSource() {
		setBasename("messages");
	}

	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new DefaultMessageSource());
	}
}
