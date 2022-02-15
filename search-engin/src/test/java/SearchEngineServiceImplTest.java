import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchEngineServiceImplTest {

    private SearchEngineServiceImpl unit = new SearchEngineServiceImpl();

    @Test
    void matchTest() {
        List<String> search = List.of("test", "test2");
        String text = "" +
                "Just random text " +
                "for testing" +
                "";

        Map<String, Set<DataSearchInfo>> actual = unit.match(0, search, text);

        assertNotNull(actual.get("test"), "should contains result");
    }

}
