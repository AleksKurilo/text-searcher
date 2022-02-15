import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class SearchServiceImplTest {

    private SearchServiceImpl unit = new SearchServiceImpl();

    @Test
    void matchTest() {
        List<String> search = List.of("Donald", "Mat", "Richard");
        List<String> text = List.of(
                "1111",
                "Donald Richard 55 $$ 666",
                "MacDonald Donald Mat Donald"
        );

        Map<String, Set<DataSearchInfo>> actual = unit.match(0, search, text);

        assertNotNull(actual.get("Donald"), "should contains result");
        assertNotNull(actual.get("Mat"), "should contains result");
        assertNotNull(actual.get("Richard"), "should contains result");
        assertEquals(3, actual.get("Donald").size(), "should contains 3 result");
        assertEquals(3, actual.get("Donald").stream().findFirst().get().lineOffset, "wrong lineOffset for given value");
        assertEquals(10, actual.get("Donald").stream().findFirst().get().charOffset, "wrong charOffset for given value");
    }
}
