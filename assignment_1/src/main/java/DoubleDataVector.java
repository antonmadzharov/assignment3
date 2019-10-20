import java.util.*;

public class DoubleDataVector implements DataVector<Double> {
    private List<String> columnNames;
    private List<Double> columnData;
    private List<String> rowNames;
    private double[] data;
    private int row;
    private String col;
    private boolean isRow;



    DoubleDataVector(double[] data, List<String> columnNames, int row) {
        this.data = data;
        this.columnNames = columnNames;
        this.row = row;
        this.isRow = true;
    }

     DoubleDataVector(List<Double> columnData, List<String> rowNames, String col) {
        this.columnData = columnData;
        this.rowNames = rowNames;
        this.col = col;
        this.isRow = false;
    }

    @Override
    public String getName() {
        if(isRow) {
            return "row_" + row;
        }
        return col;
    }

    @Override
    public List<String> getEntryNames() {
        if (isRow) {
            return new ArrayList<>(columnNames);
        }
        return new ArrayList<>(rowNames);
    }

    @Override
    public Double getValue(String entryName) {
        if(isRow) {
            Map<String, Integer> map = new HashMap<>();
            for(int i=0; i<columnNames.size();i++){
                map.put(columnNames.get(i), i);
            }
            return data[map.get(entryName)];
        }
        Map<String, Integer> map = new HashMap<>();
        for(int i=0; i<rowNames.size();i++){
            map.put(rowNames.get(i), i);
        }
        return columnData.get(map.get(entryName));
    }

    @Override
    public List<Double> getValues() {
        if(isRow) {
            List<Double> list = new ArrayList<>();
            for (double value : data) {
                list.add(value);
            }
            return list;
        }
        return columnData;
    }

    @Override
    public Map<String, Double> asMap() {
        if(isRow) {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < columnNames.size(); i++) {
                map.put(columnNames.get(i), i);
            }
            Map<String, Double> asMap = new HashMap<>();
            for (String name : columnNames) {
                asMap.put(name, data[map.get(name)]);
            }
            return asMap;
        }
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < rowNames.size(); i++) {
            map.put(rowNames.get(i), i);
        }
        Map<String, Double> asMap = new HashMap<>();
        for (String name : rowNames) {
            asMap.put(name, columnData.get(map.get(name)));
        }
        return asMap;
    }

}
