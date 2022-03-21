package cn.tendata.samples.petclinic.data.jpa.jackson.databind;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * <h2>{@link PageImpl} 对象序列化支持 {@link JsonView} 注解</h2>
 */
@SuppressWarnings("rawtypes")
public class PageSerializer extends StdSerializer<Page> {

	private static final long serialVersionUID = -9196525484747505477L;

	public PageSerializer() {
		super(Page.class);
	}

	@Override
	public void serialize(Page value, JsonGenerator gen, SerializerProvider provider)
		throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("number", value.getNumber());
		gen.writeNumberField("numberOfElements", value.getNumberOfElements());
		gen.writeNumberField("totalElements", value.getTotalElements());
		gen.writeNumberField("totalPages", value.getTotalPages());
		gen.writeNumberField("size", value.getSize());
		gen.writeFieldName("content");
		provider.defaultSerializeValue(value.getContent(), gen);
		gen.writeEndObject();
	}
}
