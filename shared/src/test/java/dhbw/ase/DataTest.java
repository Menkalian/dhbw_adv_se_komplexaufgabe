package dhbw.ase;

import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;
import dhbw.ase.util.ILoader;
import dhbw.ase.util.loader.Dataset;
import dhbw.ase.util.loader.LoaderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class DataTest {
    @Test
    void testCityDistance() {
        City first = new City("Mosbach", 241, 169);
        City second = new City("Bad Mergentheim", 245, 166);

        Assertions.assertEquals(5.0, first.distance(second));
        Assertions.assertEquals(5.0, second.distance(first));
        Assertions.assertEquals(0.0, first.distance(first));
        Assertions.assertEquals(0.0, second.distance(second));
    }

    @Test
    void testLoader() {
        ILoader loader = new LoaderImpl();
        List<City> cities = loader.loadDataset(Dataset.A280);
        Assertions.assertEquals(280, cities.size());
        Assertions.assertTrue(
                cities.stream()
                      .anyMatch(
                              (c) -> "1".equals(c.getName())
                                     && c.getX() == 288
                                     && c.getY() == 149
                      )
        );
    }

    @Test
    void testRouteDistance() {
        List<City> cities = Arrays.asList(
                new City("1", 0,0),
                new City("2", 1,0),
                new City("3", 1,1),
                new City("4", 0,1)
        );
        Route r = new Route(cities);
        Assertions.assertEquals(4.0,r.getTotalDistance());
    }
}
