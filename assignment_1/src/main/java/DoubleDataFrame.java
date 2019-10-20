import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class DoubleDataFrame implements DataFrame<Double> {
    private List<String> columnNames;
    private double[][] data;

    DoubleDataFrame(List<String> columnNames, double[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }


    @Override
    public int getColumnCount() {
        return data[0].length;
    }

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public void setValue(int rowIndex, String colName, Double value) throws IndexOutOfBoundsException, IllegalArgumentException {
        Map<String, Integer> map = toMap(columnNames);
        data[rowIndex][map.get(colName)] = value;
    }

    @Override
    public Double getValue(int rowIndex, String colName) throws IndexOutOfBoundsException, IllegalArgumentException {
        Map<String, Integer> map = toMap(columnNames);
        return data[rowIndex][map.get(colName)];
    }

    @Override
    public DataVector<Double> getRow(int rowIndex) throws IndexOutOfBoundsException {
        return new DoubleDataVector(data[rowIndex], columnNames, rowIndex);
    }

    @Override
    public DataVector<Double> getColumn(String colName) throws IllegalArgumentException {
        List<Double> list = new ArrayList<>();
        List<String> rowNames = new ArrayList<>();
        Map<String, Integer> map = toMap(columnNames);
        for (int i = 0; i < data.length; i++) {
            list.add(data[i][map.get(colName)]);
            rowNames.add("row_" + i);
        }
        return new DoubleDataVector(list, rowNames, colName);
    }

    @Override
    public List<DataVector<Double>> getRows() {
        List<DataVector<Double>> list = new ArrayList<>();
        list.add(getRow(0));
        for (String name : columnNames) {
            list.add(getColumn(name));
        }
        return list;
    }

    @Override
    public List<DataVector<Double>> getColumns() {
        List<DataVector<Double>> list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            list.add(getRow(i));
        }
        return list;
    }

    @Override
    public DataFrame<Double> expand(int additionalRows, List<String> newCols) throws IllegalArgumentException {
        List<String> newCol = new ArrayList<>();
        newCol.addAll(columnNames);
        newCol.addAll(newCols);
        double[][] newData = new double[data.length + additionalRows][columnNames.size() + newCols.size()];
        DataFrame<Double> expanded = new DoubleDataFrame(newCol, newData);
        Map<String, Integer> map = toMap(columnNames);
        for (int i = 0; i < data.length; i++) {
            for (String name : columnNames) {
                expanded.setValue(i, name, data[i][map.get(name)]);
            }
        }
        return expanded;
    }

    @Override
    public DataFrame<Double> project(Collection<String> retainColumns) throws IllegalArgumentException {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            map.put(columnNames.get(i), i);
        }
        double[][] newData = new double[data.length][retainColumns.size()];
        DataFrame<Double> df = new DoubleDataFrame(List.copyOf(retainColumns), newData);
        for (int i = 0; i < data.length; i++) {
            for (String name : retainColumns) {
                df.setValue(i, name, data[i][map.get(name)]);
            }
        }
        return df;
    }

    @Override
    public DataFrame<Double> select(Predicate<DataVector<Double>> rowFilter) {
        return null;
    }

    @Override
    public DataFrame<Double> computeColumn(String columnName, Function<DataVector<Double>, Double> function) {
        return null;
    }

    @Override
    public DataVector<Double> summarize(String name, BinaryOperator<Double> summaryFunction) {
        return null;
    }

    private Map<String, Integer> toMap(List<String> names) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            map.put(names.get(i), i);
        }
        return map;
    }
}