package logic;

import DTO.SheetDTO;
import sheet.api.Sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VersionManager implements Serializable {

    private final List<Sheet> versionedSheets = new ArrayList<>();

    public List<Sheet> getVersionedSheets() {
        return versionedSheets;
    }
    public void addSheet(Sheet newSheet) {
        if (newSheet == null) {
            throw new IllegalArgumentException("New sheet cannot be null.");
        }
        versionedSheets.add(newSheet);
    }

    public SheetDTO getSheetByVersion(int version) {
        Optional<Sheet> sheet = versionedSheets.stream()
                .filter(s -> s.getVersion() == version)
                .findFirst();

        if (sheet.isEmpty()) {
            throw new IllegalArgumentException("No sheet found with version: " + version);
        }

        return ConverterUtil.toSheetDTO(sheet.get());
    }

    public SheetDTO getLatestSheet() {
        Sheet sheet = versionedSheets.getLast();
        return ConverterUtil.toSheetDTO(sheet);
    }
}
