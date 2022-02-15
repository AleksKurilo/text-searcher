import java.util.List;
import java.util.Map;

public interface SearchService {

    Map<String, List<DataSearchInfo>> match(int partitionNumber, List<String> dataSearchList, List<String> source);

}
