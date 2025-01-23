package com.project.blog.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    String ResourceName;
    String fieldName;
    String fieldValue;

    public ResourceNotFoundException(String ResourceName, String fieldName, String fieldValue) {
        super(String.format("Resource %s not found in field %s : %s", ResourceName, fieldName, fieldValue));
        this.ResourceName = ResourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
