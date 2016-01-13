package com.company.TfL;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Data provider where data source is a file in Java (non-Android) environment
 */
public class FileDataProvider extends AbstractFileDataProvider {
    private String fileName;

    /**
     * Constructs data provider where source is read from file with given name
     *
     * @param fileName   the name of the file containing the source data
     */
    public FileDataProvider(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String dataSourceToString() throws IOException {
        InputStream is = new FileInputStream(fileName);
        return readSource(is);
    }
}
