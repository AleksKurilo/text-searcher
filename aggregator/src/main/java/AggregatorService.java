import java.util.List;
import java.util.Map;

public interface AggregatorService {

    Map<String, List<DataSearchInfo>> merge(Map<String, List<DataSearchInfo>>... maps);

}
