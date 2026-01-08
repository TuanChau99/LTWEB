package vn.iotstar.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "categories")
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c") 
public class Category {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private int id;
		@Column(columnDefinition = "NVARCHAR(255)")
		private String name;
		private int status;
	@Column(columnDefinition =  "NVARCHAR(1000)")
	private String images;

	
}
