package com.project.blog.services;

import com.project.blog.exceptions.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadFile(String path, MultipartFile file) throws IOException;
    InputStream getResource(String path, String fileName) throws ResourceNotFoundException, IOException;

}
