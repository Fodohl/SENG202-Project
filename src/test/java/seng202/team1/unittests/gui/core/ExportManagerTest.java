package seng202.team1.unittests.gui.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
<<<<<<< HEAD
import seng202.team1.data.StringTable;
import seng202.team1.data.ExportManager;
=======
import seng202.team1.data.ExportManager;
import seng202.team1.data.table.StringTable;
>>>>>>> main

public class ExportManagerTest {
    
    private StringTable mockTable;
    private Stage mockStage;
    private File tempFile;
    private File fileToReturn;
    private MockedConstruction<FileChooser> mockedFileChooserConstruction;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        mockTable = Mockito.mock(StringTable.class);
        mockStage = Mockito.mock(Stage.class);
        tempFile = tempDir.resolve("test_output.csv").toFile();
        fileToReturn = tempFile;

        mockedFileChooserConstruction = Mockito.mockConstruction(FileChooser.class,
            (mock, context) -> {
                FileChooser.ExtensionFilter mockFilter = Mockito.mock(FileChooser.ExtensionFilter.class);
                when(mock.getExtensionFilters()).thenReturn(Mockito.spy(new javafx.collections.ModifiableObservableListBase<>() {
                    private final java.util.List<FileChooser.ExtensionFilter> list = new java.util.ArrayList<>();
                    @Override public FileChooser.ExtensionFilter get(int index) { return list.get(index); }
                    @Override public int size() { return list.size(); }
                    @Override protected void doAdd(int index, FileChooser.ExtensionFilter element) { list.add(index, element); }
                    @Override protected FileChooser.ExtensionFilter doSet(int index, FileChooser.ExtensionFilter element) { return list.set(index, element); }
                    @Override protected FileChooser.ExtensionFilter doRemove(int index) { return list.remove(index); }
                }));
                
                when(mock.showSaveDialog(any())).thenAnswer(invocation -> fileToReturn);
            });
    }

    @AfterEach
    void tearDown() {
        if (mockedFileChooserConstruction != null) {
            mockedFileChooserConstruction.close();
        }
    }

    @Test
    public void exportToCSV_StandardData_WritesCorrectFile() throws IOException {
        fileToReturn = tempFile; 
        java.util.LinkedHashSet<String> orderedHeaders = new java.util.LinkedHashSet<>(List.of("Name", "Age"));
        when(mockTable.getHeaderRow()).thenReturn(orderedHeaders);
        when(mockTable.dataSize()).thenReturn(2);
        when(mockTable.getDataRow(0)).thenReturn(new HashMap<>(Map.of("Name", "Alice", "Age", "30")));
        when(mockTable.getDataRow(1)).thenReturn(new HashMap<>(Map.of("Name", "Bob", "Age", "25")));

        ExportManager.exportToCSV(mockStage, "suggested.csv", mockTable);

        String expectedCsv = "Name,Age\n" +
                             "Alice,30\n" +
                             "Bob,25\n";
                             
        String actualCsv = Files.readString(tempFile.toPath());
        assertEquals(expectedCsv, actualCsv);
    }

    @Test
    public void exportToCSV_UserCancelsDialog_DoesNotWriteFile() {
        fileToReturn = tempFile; 
        
        when(mockTable.getHeaderRow()).thenReturn(Set.of("Name"));
        when(mockTable.dataSize()).thenReturn(0);

        ExportManager.exportToCSV(mockStage, "suggested.csv", mockTable);
    }

    @Test
    public void checkNullEscape() {
        assertEquals(ExportManager.escapeCSVValue(null), "");
    }

    @Test
    public void checkIfStringContainsBadValue(){
        assertEquals(ExportManager.escapeCSVValue(",\""), "\",\"\"\"");
    }

}
