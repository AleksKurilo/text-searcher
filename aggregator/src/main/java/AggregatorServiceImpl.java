import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AggregatorServiceImpl implements AggregatorService {

    @Override
    public Map<String, List<DataSearchInfo>> merge(Map<String, List<DataSearchInfo>>... maps) {
        Map<String, List<DataSearchInfo>> result = new HashMap<>();
        for (Map<String, List<DataSearchInfo>> r : maps) {
            r.forEach((key, value) -> {
                result.computeIfPresent(key, (key1, value1) -> add(value1, value));
                result.putIfAbsent(key, value);
            });
        }

        return result;
    }

    private List<DataSearchInfo> add(List<DataSearchInfo> target, List<DataSearchInfo> value) {
        target.addAll(value);
        return target;
    }

}
