package cn.tendata.samples.petclinic.controller.util;

import cn.tendata.samples.petclinic.data.jpa.domain.AbstractEntity;
import java.io.Serializable;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class WebUtils {

	public static <T extends AbstractEntity<?>> ResponseEntity<Page<T>> pageResponse(Page<T> page) {
		return ResponseEntity.ok(page);
	}

	public static <T extends AbstractEntity<?>> ResponseEntity<T> fetchResponse(T entity) {
		return ResponseEntity.ok(entity);
	}

	public static <T extends AbstractEntity<?>> ResponseEntity<T> createdResponse(T entity) {
		if (entity != null) {
			Serializable id = entity.getId();
			Assert.notNull(id, "id can't be null");
			return ResponseEntity.created(toLocation(id)).body(entity);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	public static <T extends AbstractEntity<?>> ResponseEntity<T> updateResponse(T entity) {
		Serializable id = entity.getId();
		Assert.notNull(id, "id can't be null");
		return ResponseEntity.ok(entity);
	}

	public static ResponseEntity<Void> deletedResponse() {
		return ResponseEntity.noContent().build();
	}

	/**
	 * @param id persistence entity id
	 * @return URI location
	 */
	public static URI toLocation(Object id) {
		Assert.notNull(id, "id can't be null");
		return ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(id)
			.toUri();
	}
}
