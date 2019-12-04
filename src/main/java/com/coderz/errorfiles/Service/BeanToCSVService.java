package com.coderz.errorfiles.Service;

import java.io.FileWriter;
import java.util.*;

import com.coderz.errorfiles.Model.FileModel;
import com.coderz.errorfiles.Model.FileStorageProperties;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeanToCSVService {
    @Autowired
    FileStorageProperties fileStorageProperties;

    public String writeToFile(List<FileModel> items)
    {

        // name of generated csv
        String fileName = "errors.csv";
        final String CSV_LOCATION = fileStorageProperties.getUploadDir()+"/"+fileName;

        try {

            // Creating writer class to generate
            // csv file
            FileWriter writer = new
                    FileWriter(CSV_LOCATION);

            // Create Mapping Strategy to arrange the
            // column name in order
            ColumnPositionMappingStrategy mappingStrategy=
                    new ColumnPositionMappingStrategy();
            mappingStrategy.setType(FileModel.class);

            // Arrange column name as provided in below array.
            String[] columns = new String[]
                    { "Email", "Name", "Roles", "Errors" };
            mappingStrategy.setColumnMapping(columns);

            // Createing StatefulBeanToCsv object
            StatefulBeanToCsvBuilder<FileModel> builder=
                    new StatefulBeanToCsvBuilder(writer);
            StatefulBeanToCsv beanWriter =
                    builder.withMappingStrategy(mappingStrategy).build();

            // Write list to StatefulBeanToCsv object
            beanWriter.write(items);

            // closing the writer object
            writer.close();


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return  fileName;
    }
}
