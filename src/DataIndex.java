import java.util.Objects;

/**
 * 数据索引
 */
public class DataIndex {
    /**
     * 左下标
     */
    Integer leftIndex;
    /**
     * 右下标
     */
    Integer rightIndex;

    public DataIndex(Integer leftIndex, Integer rightIndex) {
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    public Integer getLeftIndex() {
        return leftIndex;
    }

    public Integer getRightIndex() {
        return rightIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataIndex dataIndex = (DataIndex) o;
        return Objects.equals(leftIndex, dataIndex.leftIndex) &&
                Objects.equals(rightIndex, dataIndex.rightIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftIndex, rightIndex);
    }

    /**
     * 左右翻转
     */
    public void turn() {
        Integer temp = rightIndex;
        rightIndex = leftIndex;
        leftIndex = temp;
    }
}
