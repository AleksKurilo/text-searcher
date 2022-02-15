import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregatorServiceImplTest {

    private AggregatorServiceImpl unit = new AggregatorServiceImpl();

    @Test
    void mergeTest(){
        Set<DataSearchInfo> data1 = new HashSet<>();
        data1.add(DataSearchInfo.builder().lineOffset(0).charOffset(1).build());
        Set<DataSearchInfo> data2 = new HashSet<>();
        data2.add(DataSearchInfo.builder().lineOffset(1).charOffset(2).build());
        Set<DataSearchInfo> data3 = new HashSet<>();
        data3.add(DataSearchInfo.builder().lineOffset(1).charOffset(2).build());

        Map<String, Set<DataSearchInfo>> map1 = Map.of("1", data1);
        Map<String, Set<DataSearchInfo>> map2 = Map.of("1", data2);
        Map<String, Set<DataSearchInfo>> map3 = Map.of("2", data3);

        Map<String, Set<DataSearchInfo>> actual = unit.merge(map1, map2, map3);

        assertEquals(2, actual.size(), "wrong size of the result map");
        assertEquals(2, actual.get("1").size(), "wrong size of the data search info list for the first map");
        assertEquals(1, actual.get("2").size(), "wrong size of the data search info list for the second map");
    }
}
