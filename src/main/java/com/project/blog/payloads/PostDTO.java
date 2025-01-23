package com.project.blog.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Integer id;
    private String title;
    private String content;
    private String imageName;
    private Date addedDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@Schema(hidden = true, accessMode = Schema.AccessMode.READ_ONLY)
    private CategoryDTO category;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //@Schema(hidden = true, accessMode = Schema.AccessMode.READ_ONLY)
    private UserDTO user;

    private List<CommentDTO> comments = new ArrayList<>();

}
