package com.coderz.errorfiles.Service;

import com.coderz.errorfiles.Model.FileModel;

import java.util.List;

public interface UserService {
    public void save(List<FileModel> fileModel);
    public List<String> allExistedEmail();
}
