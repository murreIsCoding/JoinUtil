import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JoinUtil {
    /**
     * 获取左联结的索引,以keyColumn列作为索引
     *
     * @param columnA A表主键的一列值
     * @param columnB B表主键的一列值
     * @return
     */
    public static List<DataIndex> getLeftJoinDataIndex(List<Object> columnA, List<Object> columnB) {
        return getJoinDataIndex(columnA, columnB, true);
    }

    public static List<DataIndex> getRightJoinDataIndex(List<Object> columnA, List<Object> columnB) {
        List<DataIndex> indexs = getLeftJoinDataIndex(columnB, columnA);
        //翻转
        for (DataIndex dataIndex : indexs) {
            dataIndex.turn();
        }
        return indexs;
    }

    public static List<DataIndex> getInnerJoinDataIndex(List<Object> columnA, List<Object> columnB) {
        return getJoinDataIndex(columnA, columnB, false);
    }

    public static List<DataIndex> getFullJoinDataIndex(List<Object> columnA, List<Object> columnB) {
        List<DataIndex> leftIndex = getLeftJoinDataIndex(columnA, columnB);
        List<DataIndex> rightIndex = getRightJoinDataIndex(columnA, columnB);
        //去重
        List<DataIndex> collect = Stream.of(leftIndex, rightIndex)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        return collect;
    }


    /**
     * 通过索引来取值
     *
     * @param dataIndexs
     * @param tableA
     * @param tableB
     * @return
     */
    public static List<Map<String, Object>> useIndexToExtractDataFromTable(List<DataIndex> dataIndexs, List<Map<String, Object>> tableA, List<Map<String, Object>> tableB) {
        List<Map<String, Object>> resultTable = new ArrayList<>();

        for (DataIndex dataIndex : dataIndexs) {
            Map<String, Object> newLine = new LinkedHashMap<>();
            if (dataIndex.leftIndex != null) {
                Map<String, Object> lineOfA = tableA.get(dataIndex.leftIndex);
                newLine.put("主键A", lineOfA.get("主键"));
                newLine.put("普通列A", lineOfA.get("普通列"));
            } else {
                Map<String, Object> emptyLine = new LinkedHashMap<>();
                emptyLine.put("主键A", null);
                emptyLine.put("普通列A", null);
                newLine.putAll(emptyLine);
            }

            if (dataIndex.rightIndex != null) {
                Map<String, Object> lineOfB = tableB.get(dataIndex.rightIndex);
                newLine.put("主键B", lineOfB.get("主键"));
                newLine.put("普通列B", lineOfB.get("普通列"));
            } else {
                Map<String, Object> emptyLine = new LinkedHashMap<>();
                emptyLine.put("主键B", null);
                emptyLine.put("普通列B", null);
                newLine.putAll(emptyLine);
            }
            resultTable.add(newLine);
        }
        return resultTable;
    }


    /**
     * @param columnA
     * @param columnB
     * @param keepNull 是否保留null的索引
     * @return
     */
    private static List<DataIndex> getJoinDataIndex(List<Object> columnA, List<Object> columnB, boolean keepNull) {
        List<DataIndex> dataIndexs = new ArrayList<>();
        for (int i = 0; i < columnA.size(); i++) {
            Object valueOfA = columnA.get(i);
            //右边是否有命中值
            boolean hasHit = false;
            for (int j = 0; j < columnB.size(); j++) {
                Object valueOfB = columnB.get(j);
                if (valueOfA.equals(valueOfB)) {
                    hasHit = true;
                    dataIndexs.add(new DataIndex(i, j));
                }
            }
            //如果没有命中，索引增加一个空值
            if (keepNull) {
                if (hasHit == false) {
                    dataIndexs.add(new DataIndex(i, null));
                }
            }
        }
        return dataIndexs;
    }
}
