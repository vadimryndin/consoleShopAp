package pl.coherentsolutions.service.util.configreader;

import pl.coherentsolutions.service.exception.ConfigReaderException;

import java.util.List;

public interface ConfigReader {
    List<?> readConfig(String filePath) throws ConfigReaderException;
}
