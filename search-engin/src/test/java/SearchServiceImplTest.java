import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class SearchServiceImplTest {

    private SearchServiceImpl unit = new SearchServiceImpl();

    @Test
    void matchTest() {
        List<String> search = List.of("test", "test2");
        List<String> text = List.of("1111", "test 55 $$ 666", "111111 11111 test2");

        Map<String, List<DataSearchInfo>> actual = unit.match(0, search, text);

        assertNotNull(actual.get("test"), "should contains result");
        assertEquals(2, actual.get("test").size(), "should contains 2 result");
        assertEquals(2, actual.get("test").get(0).lineOffset, "wrong lineOffset for given value");
        assertEquals(0, actual.get("test").get(0).charOffset, "wrong charOffset for given value");
    }
}
