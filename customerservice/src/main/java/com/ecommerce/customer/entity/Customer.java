package com.ecommerce.customer.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**Entity for Customer service
 * 
 * @author Suresh Injeti
 *
 */
@Data
@RequiredArgsConstructor
@Entity
@Table(name="customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="customer_id")
	private Integer cutomerId;
	private String name;
	private String email;
	private String phone;
	
	@CreationTimestamp
	private Timestamp created_at;

	@ToString.Include(name = "email")
    private String maskedEmail() {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String prefix = parts[0];
        return prefix.charAt(0) + "***@" + parts[1];
    }

    @ToString.Include(name = "phone")
    private String maskedMobile() {
        if (phone == null || phone.length() < 4) return phone;
        return phone.substring(0, 2) + "****" + phone.substring(phone.length() - 2);
    }
	
}
