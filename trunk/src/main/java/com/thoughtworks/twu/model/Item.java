package com.thoughtworks.twu.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@DecimalMax("99999")
    @NotNull
	private BigDecimal price;

	@NotNull
	private String description;

    @NotEmpty
    @NotNull
    private String type;

	/* Getters and setters below this row */

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
	
}
