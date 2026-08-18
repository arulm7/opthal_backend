package com.example.opthal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath;

    public FileStorageService(@Value("${app.upload.answer-images-dir}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + uploadDir, e);
        }
    }

    public String storeAnswerImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        } else {
            extension = ".jpg";
        }

        // Validate image file extension
        if (!extension.matches("^\\.(jpg|jpeg|png|webp|gif)$")) {
            throw new IllegalArgumentException("Only image files (.jpg, .jpeg, .png, .webp, .gif) are allowed.");
        }

        String generatedFilename = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.uploadPath.resolve(generatedFilename).normalize();
            if (!targetLocation.startsWith(this.uploadPath)) {
                throw new SecurityException("Cannot store file outside target directory.");
            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return generatedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image file.", e);
        }
    }

    public Resource loadAnswerImage(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Filename cannot be empty.");
        }
        try {
            Path filePath = this.uploadPath.resolve(filename).normalize();
            if (!filePath.startsWith(this.uploadPath)) {
                throw new SecurityException("Access denied: Invalid file path.");
            }
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Image file not found: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file URL: " + filename, e);
        }
    }

    public void deleteAnswerImage(String filename) {
        if (filename == null || filename.isBlank()) {
            return;
        }
        try {
            Path filePath = this.uploadPath.resolve(filename).normalize();
            if (filePath.startsWith(this.uploadPath)) {
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            System.err.println("Could not delete image file: " + filename + " - " + e.getMessage());
        }
    }
}
