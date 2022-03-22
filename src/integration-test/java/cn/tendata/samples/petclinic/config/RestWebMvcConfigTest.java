package cn.tendata.samples.petclinic.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class RestWebMvcConfigTest {

	@Test
	void localDateTime() {
		LocalDateTime date = LocalDateTime.of(2022, 1, 1, 2, 31, 45);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateTimeFormatter.withZone(ZoneId.of("UTC"));

		LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(dateTimeFormatter);
		localDateTimeSerializer.serialize();

		String actualFormat = dateTimeFormatter.format(date);
		System.out.println(actualFormat);

	}
}
