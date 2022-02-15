import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregatorServiceImplTest {

    private AggregatorServiceImpl unit = new AggregatorServiceImpl();

    @Test
    void mergeTest(){
        List<DataSearchInfo> data1 = new ArrayList<>();
        data1.add(DataSearchInfo.builder().lineOffset(0).charOffset(1).build());
        List<DataSearchInfo> data2 = new ArrayList<>();
        data2.add(DataSearchInfo.builder().lineOffset(1).charOffset(2).build());
        List<DataSearchInfo> data3 = new ArrayList<>();
        data3.add(DataSearchInfo.builder().lineOffset(1).charOffset(2).build());

        Map<String, List<DataSearchInfo>> map1 = Map.of("1", data1);
        Map<String, List<DataSearchInfo>> map2 = Map.of("1", data2);
        Map<String, List<DataSearchInfo>> map3 = Map.of("2", data3);

        Map<String, List<DataSearchInfo>> actual = unit.merge(map1, map2, map3);

        assertEquals(2, actual.size(), "wrong size of the result map");
        assertEquals(2, actual.get("1").size(), "wrong size of the data search info list for the first map");
        assertEquals(1, actual.get("2").size(), "wrong size of the data search info list for the second map");
    }
}
