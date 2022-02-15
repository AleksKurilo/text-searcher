import java.util.*;

class SearchServiceImpl implements SearchService {

    private static final String SEPARATE_WORD_TEMPLATE = " %s ";
    private static final String SEPARATE_WORD_IN_THE_BEGINNING_LINE_TEMPLATE = "%s ";
    private static final String SEPARATE_WORD_IN_THE_END_LINE_TEMPLATE = " %s";

    @Override
    public Map<String, Set<DataSearchInfo>> match(int partitionNumber, List<String> dataSearchList, List<String> source) {
        Map<String, Set<DataSearchInfo>> result = new HashMap<>();

        for (String dataSearch : dataSearchList) {
            Map<String, Set<DataSearchInfo>> map = searching(partitionNumber, dataSearch, source);

            if (!map.isEmpty()) {
                result.putAll(map);
            }
        }

        return result;
    }

    private Map<String, Set<DataSearchInfo>> searching(int partitionNumber, String search, List<String> source) {
        int lineNumber = 0;
        Set<DataSearchInfo> dataSearchInfos = new HashSet<>();

        for (String line : source) {
            lineNumber++;
            int charOffset = searchInLine(search, line);

            if (charOffset != -1) {
                DataSearchInfo dataSearchInfo = DataSearchInfo.builder()
                        .lineOffset((partitionNumber * ConfigurationDefaults.LINE_LIMIT) + lineNumber)
                        .charOffset(charOffset)
                        .build();
                dataSearchInfos.add(dataSearchInfo);
            }
        }

        Map<String, Set<DataSearchInfo>> result = new HashMap<>();

        if (!dataSearchInfos.isEmpty()) {
            result.put(search, dataSearchInfos);
        }

        return result;
    }

    private int searchInLine(String search, String line){
        int charOffset = line.indexOf(String.format(SEPARATE_WORD_TEMPLATE, search));

        if (charOffset == -1) {
            charOffset = line.indexOf(String.format(SEPARATE_WORD_IN_THE_BEGINNING_LINE_TEMPLATE, search));
        }

        if (charOffset == -1) {
            charOffset = line.indexOf(String.format(SEPARATE_WORD_IN_THE_END_LINE_TEMPLATE, search));
        }

        return charOffset;
    }
}
