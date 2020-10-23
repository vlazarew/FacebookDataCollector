package com.mycompany.app.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class InputFilters {

    private String firstName;
    private String lastName;
    private String city;
    private String study;
    private String work;
    private String outputPath;
    private int countOfThreads;
    private int countOfUsers;
    private String folderWithHandledFiles;
    private String folderWithLoadingFiles;
    private String folderWithInputFiles;
    private String fileName;

    @SneakyThrows
    public static List<InputFilters> readInputFiltersFromFolder() {
        List<InputFilters> resultList = new ArrayList<>();

        String folderWithHandledFiles = "";
        String folderWithLoadingFiles = "";
        String folderWithInputFiles = "";

        String workingDir = System.getProperty("user.dir");
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(workingDir + "/facebookCollector/src/main/resources/init.properties");
            property.load(fis);

            folderWithHandledFiles = property.getProperty("init.pathToHandledFiles");
            folderWithLoadingFiles = property.getProperty("init.pathToLoadingFiles");
            folderWithInputFiles = property.getProperty("init.pathToInputFiles");
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

        getInputFiltersFromFiles(resultList, folderWithHandledFiles, folderWithLoadingFiles, folderWithInputFiles);

        return resultList;
    }

    @SneakyThrows
    private static void getInputFiltersFromFiles(List<InputFilters> resultList, String folderWithHandledFiles,
                                                 String folderWithLoadingFiles, String folderWithInputFiles) {
        ArrayList<File> allFiles = new ArrayList<>();

        File queryDir = new File(folderWithInputFiles);
        List<File> filesInput = Arrays.asList(Objects.requireNonNull(queryDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        })));
        allFiles.addAll(filesInput);

        File loadingDir = new File(folderWithLoadingFiles);
        List<File> filesLoading = Arrays.asList(Objects.requireNonNull(loadingDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        })));

        allFiles.addAll(filesLoading);
        InputFilters temp = new InputFilters();
        ObjectMapper objectMapper = new ObjectMapper();

        for (File file : allFiles) {
            try (InputStream fileStream = new FileInputStream(file)) {
                temp = objectMapper.readValue(fileStream, InputFilters.class);
                temp.setFolderWithInputFiles(folderWithInputFiles);
                temp.setFolderWithHandledFiles(folderWithHandledFiles);
                temp.setFolderWithLoadingFiles(folderWithLoadingFiles);
                temp.setFileName(file.getName());
            }

            if (checkInputFilter(temp, file.getName())) {
                resultList.add(temp);
                file.renameTo(new File(folderWithLoadingFiles, file.getName()));
            }
        }
    }

    private static boolean checkInputFilter(InputFilters inputFilter, String fileName) {

        int countOfUsers = inputFilter.getCountOfUsers();
        if (countOfUsers < 0 || countOfUsers > 10000) {
            System.out.println("Введено неадекватное кол-во пользователей для сбора информации в файле " +
                    fileName);
            return false;
        }

        if (inputFilter.getFirstName().equals("")) {
            System.out.println("Не указано имя пользователя для сбора информации в файле " +
                    fileName);
            return false;
        }

        if (inputFilter.getLastName().equals("")) {
            System.out.println("Не указана фамилия пользователя для сбора информации в файле " +
                    fileName);
            return false;
        }

        if (inputFilter.getOutputPath().equals("")) {
            System.out.println("Не указана выходная директория с данными для сбора информации в файле " +
                    fileName);
            return false;
        }

        return true;

    }

}
