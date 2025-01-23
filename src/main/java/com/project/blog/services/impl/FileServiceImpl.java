package com.project.blog.services.impl;

import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        System.out.println(name);

        String randomId = String.valueOf(UUID.randomUUID());
        String fileName = null;
        if (name != null) {
            fileName = randomId.concat(name.substring(name.lastIndexOf(".")));
        }

        String filePath = path + File.separator + fileName;

        File dest = new File(path);
        if (!dest.exists())
            dest.mkdirs();

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws ResourceNotFoundException, IOException {
        String filePath = path + File.separator + fileName;
        return Files.newInputStream(Paths.get(filePath));
    }
}
