package com.coderz.errorfiles.Service.impl;

import com.coderz.errorfiles.Model.FetchModel;
import com.coderz.errorfiles.Model.FileModel;
import com.coderz.errorfiles.Model.FileStorageProperties;
import com.coderz.errorfiles.Service.BeanToCSVService;
import com.coderz.errorfiles.Service.FileProcessorService;
import com.coderz.errorfiles.Service.RoleService;
import com.coderz.errorfiles.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FileProcessorServiceImpl  implements FileProcessorService {
    @Autowired
    FileStorageProperties fileStorageProperties;

    @Autowired
    BeanToCSVService beanToCSVService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    private FetchModel fetchModel;

    private List<String> validRoles;
    private List<String> existedEmail;
    private List<String> traverseEmailInFile;


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

    @Override
    public FetchModel processInputFile(String fileName) {
        String inputFilePath = fileStorageProperties.getUploadDir()+"/"+fileName;
        validRoles = roleService.allRole();
        existedEmail = userService.allExistedEmail();
        traverseEmailInFile = new ArrayList<>();

        List<FileModel> inputList = new ArrayList<FileModel>();

        List<FileModel> errorList = new ArrayList<FileModel>();
        List<FileModel> correctList = new ArrayList<>();

        fetchModel = new FetchModel(0,0,null);

        try{
            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv

            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            errorList = inputList.stream().filter(item -> item.getErrors() != null).collect(Collectors.toList());
            correctList = inputList.stream().filter(item -> item.getErrors() == null).collect(Collectors.toList());

            correctList.stream().forEach(idx-> {
                System.out.println(idx.getEmail()+" "+idx.getName()+" "+idx.getRoles()+" "+idx.getErrors());
            });

            userService.save(correctList);

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private Function<String, FileModel> mapToItem = (line) -> {
        String[] p = line.split(",",-1);// a CSV has comma separated lines
        FileModel item = new FileModel();
        String err = "";
        System.out.println(p.length);
        if(p.length>=3) {
            System.out.println(p[0]);
            if (p[0] != null && p[0].trim().length() > 0) {
                item.setEmail(p[0]);//<-- this is the first column in the csv file
                if(!isValid(p[0]))
                    err += "Invalid Email";
                else{
                    if(existedEmail.contains(p[0].toUpperCase()))
                        err += "Email already existed";
                    else if(traverseEmailInFile.contains(p[0].toUpperCase()))
                        err += "Valid record with this email id already existed in same file or Email Already Existed";
                }
            }
            else
                err += "Email should not be null or empty";

            if (p[1] != null && p[1].trim().length() > 0)
                item.setName(p[1]);//<-- this is the second column in the csv file
            else
            if (err.length() > 0)
                err += "#Name should not be null or empty";
            else
                err += "Name should not be null or empty";

            if (p[2] != null && p[2].trim().length() > 0) {
                item.setRoles(p[2]);//<-- this is the third column in the csv file
                String[] roles = p[2].split("#");
                System.out.println(validRoles);
                for (String role : roles) {
                    if (!validRoles.contains(role.toUpperCase()))
                        if (err.length() > 0)
                            err += "#" + "Invalid Role " + role;
                        else
                            err += "Invalid Role " + role;
                }
            } else
            if (err.length() > 0)
                err += "#Roles should not be null or empty";
            else
                err += "Roles should not be null or empty";

            System.out.println(err);
            if(err.length() > 0) {
                item.setErrors(err);
                fetchModel.setNo_of_rows_failed(fetchModel.getNo_of_rows_failed()+1);
            }
            else
                traverseEmailInFile.add(p[0].toUpperCase());
        }

        fetchModel.setNo_of_rows_parsed(fetchModel.getNo_of_rows_parsed()+1);
        //more initialization goes here
        return item;
    };
}
