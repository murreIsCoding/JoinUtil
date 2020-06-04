import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Demo {
    public static void main(String[] args) {
        //表1
        List<Map<String, Object>> tableA = new ArrayList();
        //表2
        List<Map<String, Object>> tableB = new ArrayList();
        makeTestData(tableA, tableB);

        List<Object> columnA = getKeyColumnValue(tableA);
        List<Object> columnB = getKeyColumnValue(tableB);
        //左连接
        List<DataIndex> dataIndexsLeft = JoinUtil.getLeftJoinDataIndex(columnA, columnB);
        //右连接
        List<DataIndex> dataIndexsRight = JoinUtil.getRightJoinDataIndex(columnA, columnB);
//        //内连接
        List<DataIndex> dataIndexsInner = JoinUtil.getInnerJoinDataIndex(columnA, columnB);
        //全连接
        List<DataIndex> dataIndexsFull = JoinUtil.getFullJoinDataIndex(columnA, columnB);
        List<Map<String, Object>> resultLeft = JoinUtil.useIndexToExtractDataFromTable(dataIndexsLeft, tableA, tableB);
        List<Map<String, Object>> resultRight = JoinUtil.useIndexToExtractDataFromTable(dataIndexsRight, tableA, tableB);
        List<Map<String, Object>> resultInner = JoinUtil.useIndexToExtractDataFromTable(dataIndexsInner, tableA, tableB);
        List<Map<String, Object>> resultFull = JoinUtil.useIndexToExtractDataFromTable(dataIndexsFull, tableA, tableB);
        printOrgTable(tableA);
        printOrgTable(tableB);
        print("左连接",resultLeft);
        print("右连接",resultRight);
        print("内连接",resultInner);
        print("全连接",resultFull);
    }


    //获取表的主键列的值
    private static List<Object> getKeyColumnValue(List<Map<String, Object>> tableA) {
        List<Object> columns = new ArrayList<>();
        for (Map<String, Object> line : tableA) {
            if (line.containsKey("主键")) {
                columns.add(line.get("主键"));
            } else {
                columns.add(null);
            }
        }
        return columns;
    }

    /**
     * 格式化打印表
     *
     * @param result
     */
    private static void printOrgTable(List<Map<String, Object>> result) {
        for (Map<String, Object> line : result) {
            System.out.print(line.get("主键") + "\t");
            System.out.print(line.get("普通列") + "\r\n");
        }
        System.out.print("\r\n");
    }

    /**
     * 格式化打印表
     *
     * @param result
     */
    private static void print(String title,List<Map<String, Object>> result) {
        System.out.println(title);
        for (Map<String, Object> line : result) {
            System.out.print(line.get("主键A") + "\t");
            System.out.print(line.get("普通列A") + "\t");
            System.out.print(line.get("主键B") + "\t");
            System.out.print(line.get("普通列B") + "\r\n");
        }
        System.out.print("\r\n");
    }

    /**
     * 测试数据
     *
     * @param tableA
     * @param tableB
     */
    private static void makeTestData(List<Map<String, Object>> tableA, List<Map<String, Object>> tableB) {
        tableA.add(new LinkedHashMap<String, Object>() {{
            put("主键", "1");
            put("普通列", "X1");
        }});
        tableA.add(new LinkedHashMap<String, Object>() {{
            put("主键", "2");
            put("普通列", "X2");
        }});
        tableA.add(new LinkedHashMap<String, Object>() {{
            put("主键", "3");
            put("普通列", "X3");
        }});

        tableB.add(new LinkedHashMap<String, Object>() {{
            put("主键", "4");
            put("普通列", "Y1");
        }});
        tableB.add(new LinkedHashMap<String, Object>() {{
            put("主键", "5");
            put("普通列", "Y2");
        }});
        tableB.add(new LinkedHashMap<String, Object>() {{
            put("主键", "6");
            put("普通列", "Y3");
        }});
    }

}
