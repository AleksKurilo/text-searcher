import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Builder
public class SearchingTask extends RecursiveTask<Map<String, List<DataSearchInfo>>> {

    static Logger logger = Logger.getLogger(SearchingTask.class.getName());

    private final int partitionNumber;
    private final List<String> dataSearchList;
    private final List<String> subSource;
    private final List<List<String>> source;

    private final transient SearchServiceImpl matcherServiceImpl;

    @Override
    protected Map<String, List<DataSearchInfo>> compute() {
        if (source != null) {
            List<Map<String, List<DataSearchInfo>>> resultList = ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .collect(Collectors.toList());

            Map<String, List<DataSearchInfo>> result = new HashMap<>();
            for (Map<String, List<DataSearchInfo>> r : resultList) {
                r.forEach((key, value) -> {
                    result.computeIfPresent(key, (key1, value1) -> add(value1, value));
                    result.putIfAbsent(key, value);
                });
            }

            return result;

        } else {
            return processing(partitionNumber, subSource);
        }
    }

    private List<SearchingTask> createSubtasks() {
        List<SearchingTask> tasks = new ArrayList<>();

        for (int partition = 0; partition < source.size(); partition++) {
            SearchingTask task = SearchingTask.builder()
                    .partitionNumber(partition)
                    .dataSearchList(this.dataSearchList)
                    .subSource(source.get(partition))
                    .matcherServiceImpl(new SearchServiceImpl())
                    .build();

            tasks.add(task);
        }

        return tasks;
    }

    private Map<String, List<DataSearchInfo>> processing(int partitionNumber, List<String> subSource) {
        Map<String, List<DataSearchInfo>> result = new HashMap<>();

        Map<String, List<DataSearchInfo>> map = matcherServiceImpl.match(partitionNumber, dataSearchList, subSource);
        map.forEach((key, value) -> {
            result.computeIfPresent(key, (key1, value1) -> add(value1, value));
            result.putIfAbsent(key, value);
        });
        partitionNumber++;


        return result;
    }

    private List<DataSearchInfo> add(List<DataSearchInfo> target, List<DataSearchInfo> value) {
        target.addAll(value);
        return target;
    }

}
