###How to setup

- Put `<aop:aspectj-autoproxy proxy-target-class="true"/>` to your dispatcher-servlet.xml, Coz it implements AOP based on AspectJ
- Make Sure LocalCacheAspect and LocalCacheManager initialized by your container.

###How to use
```java
@Service
public class ExampleService {
    @LocalCacheAnnotation(name = WEEKLY_RANK_INFO, expireInMillis = 1000 * 30)
    public Map<String, Data> getWeeklyRankInfo(Integer categoryId) {
        Map<String, Data> statRankWeekDataMap = new HashMap<String, Data>();
        List<Data> statRankWeekDataList = this.facadeMysqlDao.getStatRankWeekDataDao().listLatestByStatDateAndType(categoryId, 0, 100);
        for (Data statData: statRankWeekDataList) {
            statRankWeekDataMap.put(statData.getPid(), statData);
        }
        return statRankWeekDataMap;
    }
}
```
