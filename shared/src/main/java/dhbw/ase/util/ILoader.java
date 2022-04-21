package dhbw.ase.util;

import dhbw.ase.tsp.City;
import dhbw.ase.util.loader.Dataset;

import java.util.List;

public interface ILoader {
    List<City> loadDataset(Dataset dataset);
}
