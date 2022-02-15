import java.util.*;
import java.util.stream.Collectors;

class SearchServiceImpl implements SearchService {

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
            int lineOffset = (partitionNumber * ConfigurationDefaults.LINE_LIMIT) + lineNumber;
            dataSearchInfos.addAll(searchInLine(lineOffset, search, line));
        }

        Map<String, Set<DataSearchInfo>> result = new HashMap<>();

        if (!dataSearchInfos.isEmpty()) {
            result.put(search, dataSearchInfos);
        }

        return result;
    }

    private Set<DataSearchInfo> searchInLine(int lineOffset, String search, String line) {
        int matherCount = (int) Arrays.stream(line.split(" "))
                .filter(word -> word.equals(search)).count();

        if (matherCount == 0) {
            return Collections.emptySet();
        }

        List<Integer> charOffsetList = new ArrayList<>();

        for (int i = 0; i < matherCount; i++) {
            int charOffset = line.indexOf(search);

            if (charOffsetList.contains(charOffset)) {
                int index = line.indexOf(search, charOffset + 1);
                charOffsetList.add(index);
            } else {
                charOffsetList.add(charOffset);
            }
        }

        return charOffsetList.stream()
                .map(charOffset -> DataSearchInfo.builder()
                        .lineOffset(lineOffset)
                        .charOffset(charOffset)
                        .build()
                )
                .collect(Collectors.toSet());

    }
}
