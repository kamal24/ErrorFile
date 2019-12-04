package com.coderz.errorfiles.controller;

import com.coderz.errorfiles.Model.FetchModel;
import com.coderz.errorfiles.Model.FileModel;
import com.coderz.errorfiles.Model.FileStorageProperties;
import com.coderz.errorfiles.Service.BeanToCSVService;
import com.coderz.errorfiles.Service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController

public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    FileStorageProperties fileStorageProperties;

    @Autowired
    BeanToCSVService beanToCSVService;

    private FetchModel fetchModel;

    private List<String> validRoles = new ArrayList<>(Arrays.asList("SA","ADMIN","USER"));

    private static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private List<FileModel> processInputFile(String inputFilePath) {
        List<FileModel> inputList = new ArrayList<FileModel>();

        fetchModel = new FetchModel(0,0,null);

        try{
            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().skip(1).map(mapToItem).filter(item -> item.getErrors() != null).collect(Collectors.toList());
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputList.stream().forEach(idx-> {
            System.out.println(idx.getEmail()+" "+idx.getName()+" "+idx.getRoles()+" "+idx.getErrors());
        });

        return inputList;
    }

    private Function<String, FileModel> mapToItem = (line) -> {
        String[] p = line.split(",");// a CSV has comma separated lines
        FileModel item = new FileModel();
        String err = "";
        if(p.length>=3) {
            //System.out.println(p[0]);
            if (p[0] != null && p[0].trim().length() > 0) {
                item.setEmail(p[0]);//<-- this is the first column in the csv file
                if(!isValid(p[0]))
                    err= "Invalid Email";
            }

            if (p[1] != null && p[1].trim().length() > 0)
                item.setName(p[1]);//<-- this is the second column in the csv file

            if (p[2] != null && p[2].trim().length() > 0) {
                item.setRoles(p[2]);//<-- this is the third column in the csv file
                String[] roles = p[2].split("#");

                for (String role : roles) {
                    if (!validRoles.contains(role))
                        if (err.length() > 0)
                            err += "#" + "Invalid Role " + role;
                        else
                            err += "Invalid Role " + role;
                };
            }
        }

        if(err.length() > 0) {
            item.setErrors(err);
            fetchModel.setNo_of_rows_failed(fetchModel.getNo_of_rows_failed()+1);
        }

        fetchModel.setNo_of_rows_parsed(fetchModel.getNo_of_rows_parsed()+1);
        //more initialization goes here
        return item;
    };

    @PostMapping("/register")
    public FetchModel uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        List<FileModel> errorList = processInputFile(fileStorageProperties.getUploadDir()+"/"+fileName);

        if(errorList.size()>0) {
            fileName = beanToCSVService.writeToFile(fileName,errorList);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(fileName)
                    .toUriString();

            fetchModel.setError_file_url(fileDownloadUri);
        }

        return fetchModel;
    }

    @GetMapping("/download/{file}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String file, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(file);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
