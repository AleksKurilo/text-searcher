import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SearchServiceImpl implements SearchService {

    @Override
    public Map<String, List<DataSearchInfo>> match(int partitionNumber, List<String> dataSearchList, List<String> source) {
        Map<String, List<DataSearchInfo>> result = new HashMap<>();

        for (String dataSearch : dataSearchList) {
            Map<String, List<DataSearchInfo>> map = searching(partitionNumber, dataSearch, source);

            if (!map.isEmpty()) {
                result.putAll(map);
            }
        }

        return result;
    }

    private Map<String, List<DataSearchInfo>> searching(int partitionNumber, String search, List<String> source) {
        int lineNumber = 0;
        List<DataSearchInfo> dataSearchInfos = new ArrayList<>();

        for (String line : source) {
            lineNumber++;
            int charOffset = line.indexOf(search);

            if (charOffset != -1) {
                DataSearchInfo dataSearchInfo = DataSearchInfo.builder()
                        .lineOffset((partitionNumber * ConfigurationDefaults.LINE_LIMIT) + lineNumber)
                        .charOffset(charOffset)
                        .build();
                dataSearchInfos.add(dataSearchInfo);
            }
        }

        Map<String, List<DataSearchInfo>> result = new HashMap<>();

        if (!dataSearchInfos.isEmpty()) {
            result.put(search, dataSearchInfos);
        }

        return result;
    }
}
