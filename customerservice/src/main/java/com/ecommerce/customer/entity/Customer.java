package com.ecommerce.customer.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;

/**Entity for Customer service
 * 
 * @author Suresh Injeti
 *
 */
//@Data
//@RequiredArgsConstructor
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
	private LocalDateTime created_at;

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

	public Integer getCutomerId() {
		return cutomerId;
	}

	public void setCutomerId(Integer cutomerId) {
		this.cutomerId = cutomerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
    
	
}
