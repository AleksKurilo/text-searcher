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
        List<String> search = List.of("test", "test2");
        List<String> text = List.of("1111", "test 55 $$ 666", "111--test--11 11111 test2");

        Map<String, Set<DataSearchInfo>> actual = unit.match(0, search, text);

        assertNotNull(actual.get("test"), "should contains result");
        assertEquals(2, actual.get("test").size(), "should contains 1 result");
        assertEquals(3, actual.get("test2").stream().findFirst().get().lineOffset, "wrong lineOffset for given value");
        assertEquals(19, actual.get("test2").stream().findFirst().get().charOffset, "wrong charOffset for given value");
    }
}
