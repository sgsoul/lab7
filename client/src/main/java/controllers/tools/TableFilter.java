package controllers.tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableFilter<T> {
    private ObservableResourceFactory resourceFactory;
    private TableView<T> table;
    private ObservableList<T> list;
    private Map<TableColumn<T, ?>, FilterArg> columnsFilters;

    public TableFilter(TableView<T> tab, ObservableList<T> l, ObservableResourceFactory res) {
        table = tab;
        list = l;
        resourceFactory = res;
        columnsFilters = new HashMap<>();
    }

    public TableFilter<T> addFilter(TableColumn<T, ?> col, Converter<T> converter) {

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setAutoHide(true);
        contextMenu.setHideOnEscape(true);
        contextMenu.setStyle("-fx-max-height: 300;");


        TextField filter = new TextField();
        CustomMenuItem filterItem = new CustomMenuItem(filter);
        filterItem.setHideOnClick(false);

        MenuItem ok = new MenuItem();
        ok.textProperty().bind(resourceFactory.getStringBinding("OK"));

        String defaultHeaderStyle = col.getStyle();

        ok.setOnAction((event) -> {
            String condition = filter.getText();
            if (condition != null && !condition.equals("")) {
                columnsFilters.put(col, new FilterArg(condition, converter));
                Pattern pattern = Pattern.compile(condition, Pattern.CASE_INSENSITIVE);
                updateFilters();
                col.getStyleClass().add("filtered");

            }
        });

        MenuItem reset = new MenuItem();
        reset.textProperty().bind(resourceFactory.getStringBinding("ResetFilter"));

        reset.setOnAction((event) -> {
            resetFilter(col);
        });


        contextMenu.getItems().addAll(filterItem, ok, reset);
        col.setContextMenu(contextMenu);
        return this;
    }

    public TableFilter<T> resetFilter(TableColumn<T, ?> col) {
        col.getStyleClass().remove("Фильтр успешно применен.");
        columnsFilters.remove(col);
        updateFilters();
        return this;
    }

    public TableFilter<T> updateFilters() {
        Stream<T> stream = list.stream();
        for (Map.Entry<TableColumn<T, ?>, FilterArg> entry : columnsFilters.entrySet()) {
            TableColumn<T, ?> col = entry.getKey();
            String condition = entry.getValue().condition;
            Converter<T> converter = entry.getValue().converter;
            if (condition != null && !condition.equals("")) {
                Pattern pattern = Pattern.compile(condition, Pattern.CASE_INSENSITIVE);

                stream = stream.filter((p) -> pattern.matcher(converter.convert(p) != null ? converter.convert(p) : "").find());
                col.getStyleClass().add("Фильтр успешно применен.");
            }

        }
        ObservableList<T> filteredList = stream.collect(Collectors.toCollection(FXCollections::observableArrayList));
        table.setItems(filteredList);
        return this;
    }

    public TableFilter<T> filter(TableColumn<T, ?> column, String condition, Converter<T> converter) {
        resetFilter(column);
        columnsFilters.put(column, new FilterArg(condition, converter));
        column.getStyleClass().add("Фильтр успешно применен.");
        updateFilters();
        return this;
    }

    public void resetFilters() {
        for (TableColumn<T, ?> col : columnsFilters.keySet()) {
            col.getStyleClass().remove("Фильтр успешно применен.");
        }
        columnsFilters.clear();
        table.setItems(list);

    }

    private class FilterArg {
        public final String condition;
        public final Converter<T> converter;

        public FilterArg(String cond, Converter<T> conv) {
            converter = conv;
            condition = cond;
        }
    }
}
