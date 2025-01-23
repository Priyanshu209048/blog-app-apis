package com.project.blog.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer categoryId;

    @NotBlank(message = "Category Name can not be empty !!")
    @Size(min = 2,max = 20,message = "Category name must be between 2 - 20 characters")
    private String categoryName;

    @NotBlank(message = "Category Description cannot be empty")
    @Size(min = 10, max = 100, message = "Category description must be between 10 - 100 characters")
    private String categoryDesc;

}
