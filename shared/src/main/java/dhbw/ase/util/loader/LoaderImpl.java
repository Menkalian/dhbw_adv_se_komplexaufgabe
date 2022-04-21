package dhbw.ase.util.loader;

import dhbw.ase.tsp.City;
import dhbw.ase.util.ILoader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LoaderImpl implements ILoader {
    @Override
    public List<City> loadDataset(Dataset dataset) {
        try {
            String data = loadFileFromResources(dataset.getResourceName());
            return parseCities(data);
        } catch (Exception ex) {
            throw new RuntimeException("Unerwarteter Fehler beim Laden des Datensets.");
        }
    }

    private String loadFileFromResources(String filename) throws Exception {
        URL resource = this.getClass()
                           .getClassLoader()
                           .getResource(filename);

        if (resource != null) {
            try (InputStream inStream = resource.openStream()) {
                byte[] read = inStream.readAllBytes();
                return new String(read, StandardCharsets.UTF_8);
            } catch (Exception ex) {
                throw new Exception("Ressouce konnte nicht geladen werden", ex);
            }
        }

        throw new FileNotFoundException("Die angegebene Ressource konnte nicht gefunden werden.");
    }

    private List<City> parseCities(String data) {
        Pattern validLinePattern = Pattern.compile("\\S+ \\d{1,3} \\d{1,3}");
        return data.lines()
                   .filter((s) -> validLinePattern.matcher(s).matches())
                   .map((s) -> {
                       String[] sections = s.split(" ");
                       return new City(
                               sections[0],
                               Integer.parseInt(sections[1]),
                               Integer.parseInt(sections[2])
                       );
                   })
                   .collect(Collectors.toList());

    }
}
