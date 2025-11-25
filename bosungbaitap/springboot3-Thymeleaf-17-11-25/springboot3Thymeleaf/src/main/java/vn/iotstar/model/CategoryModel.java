package vn.iotstar.model;

import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.*;

@Data

@AllArgsConstructor

@NoArgsConstructor

public class CategoryModel {

	private Long categoryId;

//validate

	@NotEmpty

	@Length(min = 2)

	private String name;

	private Boolean isEdit = false;

}