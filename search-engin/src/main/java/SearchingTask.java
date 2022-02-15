import lombok.Builder;

import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

@Builder
public class SearchingTask extends RecursiveTask<Map<String, Set<DataSearchInfo>>> {

    private final int partitionNumber;
    private final List<String> dataSearchList;
    private final List<String> subSource;
    private final List<List<String>> source;

    private final transient SearchServiceImpl matcherServiceImpl;

    @Override
    protected Map<String, Set<DataSearchInfo>> compute() {
        if (source != null) {
            List<Map<String, Set<DataSearchInfo>>> resultList = ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .collect(Collectors.toList());

            Map<String, Set<DataSearchInfo>> result = new HashMap<>();
            for (Map<String, Set<DataSearchInfo>> r : resultList) {
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

        for (List<String> strings : source) {
            SearchingTask task = SearchingTask.builder()
                    .partitionNumber(this.partitionNumber)
                    .dataSearchList(this.dataSearchList)
                    .subSource(strings)
                    .matcherServiceImpl(new SearchServiceImpl())
                    .build();

            tasks.add(task);
        }

        return tasks;
    }

    private Map<String, Set<DataSearchInfo>> processing(int partitionNumber, List<String> subSource) {
        Map<String, Set<DataSearchInfo>> result = new HashMap<>();
        Map<String, Set<DataSearchInfo>> map = matcherServiceImpl.match(partitionNumber, dataSearchList, subSource);
        map.forEach((key, value) -> {
            result.computeIfPresent(key, (key1, value1) -> add(value1, value));
            result.putIfAbsent(key, value);
        });
        partitionNumber++;

        return result;
    }

    private Set<DataSearchInfo> add(Set<DataSearchInfo> target, Set<DataSearchInfo> value) {
        target.addAll(value);
        return target;
    }

}
