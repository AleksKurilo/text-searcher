import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregatorBuilder {

    public static  AggregatorService aggregatorService(){
        return new AggregatorServiceImpl();
    }

    public static  OutputService outputService(){
        return new OutputServiceImpl();
    }

}
