package Datamaskin.Filbehandling;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileSaverSpecificOrder {

    static void makeString(Path path, String str) throws IOException {
        Files.write(path, str.getBytes());
    }

    String DELIMITER = ";";




}
