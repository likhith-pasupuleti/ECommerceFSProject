package com.ecommerce.sb_ecomProject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService
{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException
    {
        // Get the original file name
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null)
        {
            throw new IOException("Invalid file name");
        }

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = randomId.concat(fileExtension);

        // Ensure the path exists
        File folder = new File(path);
        if (!folder.exists())
        {
            folder.mkdirs(); // Creates all missing directories
        }

        // Save the file
        String filePath = path + File.separator + fileName;         //(File Separator -> /)

        //Read the file as InputStream and convert the string path to a proper Path Object
        //Copying the File to the Destination Path in input stream format
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName; // Return saved file name
    }
}
