package com.coderz.errorfiles.Service;


import com.coderz.errorfiles.Model.FileModel;

import java.util.List;

public interface BeanToCSVService {
   public String writeToFile(String inputFile, List<FileModel> items);
}
