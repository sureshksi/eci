package com.ecommerce.inventory.pojo;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**Idempotency support to avoid multiple submits
 * 
 * @author Suresh Injeti
 *
 */
@Data
@RequiredArgsConstructor
public class IdempotencyResponse {
	private String responseBody;
    private HttpStatus responseStatus;
    private LocalDateTime createdAt = LocalDateTime.now();
}
