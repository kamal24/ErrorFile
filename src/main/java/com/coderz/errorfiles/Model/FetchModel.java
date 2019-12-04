package com.coderz.errorfiles.Model;

import java.util.Objects;

public class FetchModel {
    private int no_of_rows_parsed;
    private int no_of_rows_failed;
    private String error_file_url;

    public FetchModel(int no_of_rows_parsed,
             int no_of_rows_failed,
             String error_file_url){
        this.error_file_url = error_file_url;
        this.no_of_rows_failed = no_of_rows_failed;
        this.no_of_rows_parsed = no_of_rows_parsed;
    }

    public int getNo_of_rows_parsed() {
        return no_of_rows_parsed;
    }

    public void setNo_of_rows_parsed(int no_of_rows_parsed) {
        this.no_of_rows_parsed = no_of_rows_parsed;
    }

    public int getNo_of_rows_failed() {
        return no_of_rows_failed;
    }

    public void setNo_of_rows_failed(int no_of_rows_failed) {
        this.no_of_rows_failed = no_of_rows_failed;
    }

    public String getError_file_url() {
        return error_file_url;
    }

    public void setError_file_url(String error_file_url) {
        this.error_file_url = error_file_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FetchModel that = (FetchModel) o;
        return no_of_rows_parsed == that.no_of_rows_parsed &&
                no_of_rows_failed == that.no_of_rows_failed &&
                Objects.equals(error_file_url, that.error_file_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no_of_rows_parsed, no_of_rows_failed, error_file_url);
    }

    @Override
    public String toString() {
        return "FetchModel{" +
                "no_of_rows_parsed=" + no_of_rows_parsed +
                ", no_of_rows_failed=" + no_of_rows_failed +
                ", error_file_url='" + error_file_url + '\'' +
                '}';
    }
}
