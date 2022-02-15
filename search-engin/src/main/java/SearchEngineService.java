import java.util.List;
import java.util.Map;

public interface SearchEngineService {

    Map<String, List<DataSearchInfo>> match(List<String> search, String text);
}
