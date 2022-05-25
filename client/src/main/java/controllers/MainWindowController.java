package controllers;


import common.connection.CollectionOperation;
import common.connection.CommandMsg;
import common.connection.Request;
import common.connection.Response;
import common.data.*;
import common.exceptions.*;
import common.utils.DateConverter;
import controllers.tools.MapUtils;
import controllers.tools.TableFilter;
import controllers.tools.ZoomOperator;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import main.App;
import client.Client;
import controllers.tools.ObservableResourceFactory;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Main window controller.
 */
public class MainWindowController {

    private final long RANDOM_SEED = 1821L;
    private final Duration ANIMATION_DURATION = Duration.millis(800);
    private final double MAX_SIZE = 250;

    @FXML
    private TableView<HumanBeing> humanTable;
    @FXML
    private TableColumn<HumanBeing, Integer> idColumn;
    @FXML
    private TableColumn<HumanBeing, String> ownerColumn;
    @FXML
    private TableColumn<HumanBeing, Date> creationDateColumn;
    @FXML
    private TableColumn<HumanBeing, String> nameColumn;
    @FXML
    private TableColumn<HumanBeing, Integer> impactSpeedColumn;
    @FXML
    private TableColumn<HumanBeing, Double> coordinatesXColumn;
    @FXML
    private TableColumn<HumanBeing, Double> coordinatesYColumn;
    @FXML
    private TableColumn<HumanBeing, Boolean> realHeroColumn;
    @FXML
    private TableColumn<HumanBeing, Boolean> hasToothpickColumn;
    @FXML
    private TableColumn<HumanBeing, String> soundtrackNameColumn;
    @FXML
    private TableColumn<HumanBeing, Integer> minutesOfWaitingColumn;
    @FXML
    private TableColumn<HumanBeing, WeaponType> weaponTypeColumn;
    @FXML
    private TableColumn<HumanBeing, Car> carColumn;
    @FXML
    private TableColumn<HumanBeing, Boolean> carCoolCheckColumn;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab canvasTab;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;

    @FXML
    private Button refreshButton;
    @FXML
    private Tooltip addButtonTooltip;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Tooltip removeButtonTooltip;
    @FXML
    private Tooltip clearButtonTooltip;
    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;

    private App app;
    private Tooltip shapeTooltip;
    private TableFilter<HumanBeing> tableFilter;
    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private FileChooser fileChooser;
    private AskWindowController askWindowController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Integer> shapeMap;
    private Map<Integer, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ObservableResourceFactory resourceFactory;
    private Map<String, Locale> localeMap;

    /**
     * Initialize main window.
     */
    public void initialize() {
        initializeTable();
        initCanvas();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<>();
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = new HashMap<>();
        localeMap.put("English", new Locale("en", "NZ"));
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Српски", new Locale("de", "RS"));
        localeMap.put("Polski", new Locale("da", "PL"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    /**
     * Initialize table.
     */

    private void initializeTable() {

        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getUserLogin()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        impactSpeedColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getImpactSpeed()));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        realHeroColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));
        hasToothpickColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));
        soundtrackNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getSoundtrackName()));
        minutesOfWaitingColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getMinutesOfWaiting()));
        weaponTypeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getWeaponType()));
        carColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCar()));

        creationDateColumn.setCellFactory(column -> {
            TableCell<HumanBeing, Date> cell = new TableCell<HumanBeing, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(DateConverter.dateToString(item));
                    }
                }
            };
            return cell;
        });
    }

    public void initFilter(){
        tableFilter = new TableFilter<HumanBeing>(humanTable,client.getHumanManager().getCollection(),resourceFactory)
                .addFilter(idColumn, (h)->Integer.toString(h.getId()))
                .addFilter(nameColumn, HumanBeing::getName)
                .addFilter(coordinatesXColumn, (h)->Double.toString(h.getCoordinates().getX()))
                .addFilter(coordinatesYColumn, (h)->Double.toString(h.getCoordinates().getY()))
                .addFilter(creationDateColumn, (h)-> DateConverter.dateToString(h.getCreationDate()))
                .addFilter(impactSpeedColumn, (h)->Integer.toString(h.getImpactSpeed()))
                .addFilter(soundtrackNameColumn, HumanBeing::getSoundtrackName)
                .addFilter(minutesOfWaitingColumn, (h)->Integer.toString((int) h.getMinutesOfWaiting()))
                .addFilter(impactSpeedColumn, (h)->Long.toString(h.getImpactSpeed()))
                .addFilter(weaponTypeColumn, (h)->h.getWeaponType().toString())
                .addFilter(ownerColumn, HumanBeing::getUserLogin);
    }

    public TableFilter<HumanBeing> getFilter(){
        return tableFilter;
    }
    public TableColumn<HumanBeing,?> getNameColumn(){
        return nameColumn;
    }

    private void initCanvas(){
        ZoomOperator zoomOperator = new ZoomOperator();
        canvasPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.5;
                if (event.getDeltaY() <= 0) {
                    // zoom out
                    zoomFactor = 1 / zoomFactor;

                }

                double x = event.getSceneX();
                double y = event.getSceneY();

                if((event.getDeltaY()<=0 && (zoomOperator.getBounds().getHeight()<=500||zoomOperator.getBounds().getWidth()<=500))) return;
                zoomOperator.zoom(canvasPane, zoomFactor, x, y);


            }
        });

        zoomOperator.draggable(canvasPane);
        canvasPane.setMinWidth(2000);
        canvasPane.setMinHeight(2000);
    }
    /**
     * Bind gui language.
     */

    private void bindGuiLanguage() {
        resourceFactory.setResources(ResourceBundle.getBundle
                (App.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));
        DateConverter.setPattern(resourceFactory.getRawString("DateFormat"));

        idColumn.textProperty().bind(resourceFactory.getStringBinding("IdColumn"));
        ownerColumn.textProperty().bind(resourceFactory.getStringBinding("OwnerColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        impactSpeedColumn.textProperty().bind(resourceFactory.getStringBinding("ImpactSpeedColumn"));
        realHeroColumn.textProperty().bind(resourceFactory.getStringBinding("RealHeroColumn"));
        hasToothpickColumn.textProperty().bind(resourceFactory.getStringBinding("HasToothpickColumn"));
        soundtrackNameColumn.textProperty().bind(resourceFactory.getStringBinding("SoundtrackNameColumn"));
        minutesOfWaitingColumn.textProperty().bind(resourceFactory.getStringBinding("MinutesOfWaitingColumn"));
        weaponTypeColumn.textProperty().bind(resourceFactory.getStringBinding("WeaponTypeColumn"));
        carColumn.textProperty().bind(resourceFactory.getStringBinding("CarColumn"));
        carCoolCheckColumn.textProperty().bind(resourceFactory.getStringBinding("CarCoolCheckColumn"));

        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        addButton.textProperty().bind(resourceFactory.getStringBinding("AddButton"));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeButton.textProperty().bind(resourceFactory.getStringBinding("RemoveButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        refreshButton.textProperty().bind(resourceFactory.getStringBinding("RefreshButton"));

        addButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        refreshButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RefreshButtonTooltip"));
    }

    /**
     * Refresh button on action.
     */

    @FXML
    public void refreshButtonOnAction() {
        processAction(new CommandMsg("show"));
    }

    /**
     * Info button on action.
     */

    @FXML
    private void infoButtonOnAction() {
        processAction(new CommandMsg("info"));
    }


    /**
     * Update button on action.
     */

    @FXML
    private void updateButtonOnAction() {

        HumanBeing human = humanTable.getSelectionModel().getSelectedItem();
        if(human!=null) {
            askWindowController.setHuman(human);
            try {
                processAction(new CommandMsg("update").setArgument(Integer.toString(human.getId())).setHuman(askWindowController.readHuman()));
            } catch (InvalidDataException e) {
            }
        }
    }

    /**
     * Remove button on action.
     */

    @FXML
    private void removeButtonOnAction() {
        HumanBeing human = humanTable.getSelectionModel().getSelectedItem();
        if(human!=null) processAction(new CommandMsg("remove_by_id").setArgument(Integer.toString(human.getId())));
    }


    /**
     * Clear button on action.
     */
    @FXML
    private void clearButtonOnAction() {
        client.getCommandManager().runCommand(new CommandMsg("clear"));
    }

    /**
     * Add button on action.
     */

    @FXML
    private void addButtonOnAction() {
        try {
            processAction(new CommandMsg("add").setHuman(askWindowController.readHuman()));
        } catch (InvalidDataException ignored) {

        }
    }

    /**
     * Request action.
     */

    private Response processAction(Request request){
        Response response = client.getCommandManager().runCommand(request);
        String msg = response.getMessage();

        return response;
    }


    public void refreshTable(){
        humanTable.refresh();
        tableFilter.updateFilters();
    }
    /**
     * Refreshes canvas.
     */

    public void refreshCanvas(ObservableList<HumanBeing> collection, Collection<HumanBeing> changes, CollectionOperation op) {

        for (HumanBeing human : changes) {
            if (!userColorMap.containsKey(human.getUserLogin()))
                userColorMap.put(human.getUserLogin(),
                        Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));
            if(op==CollectionOperation.ADD) {
                addToCanvas(human);
            }
            else if(op==CollectionOperation.REMOVE){
                removeFromCanvas(human.getId());
            }
            else if(op==CollectionOperation.UPDATE){
                removeFromCanvas(human.getId());
                addToCanvas(human);
            }
        }
        List<Shape> circles = new ArrayList<Shape>(shapeMap.keySet());
        circles.sort((e1,e2)->((Circle)e1).getRadius()>((Circle)e2).getRadius()?-1:0);
        List<Shape> texts = new ArrayList<>(textMap.values());
        canvasPane.getChildren().setAll(circles);
        canvasPane.getChildren().addAll(texts);

    }

    private void removeFromCanvas(Integer id){
        Shape shape = MapUtils.getKeyByValue(shapeMap,id);
        Text text = textMap.get(id);
        shapeMap.values().remove(id);
        textMap.remove(id);
        canvasPane.getChildren().remove(shape);
        canvasPane.getChildren().remove(text);
    }
    private void addToCanvas(HumanBeing human){
        double size = Math.min(human.getImpactSpeed() / 1000, MAX_SIZE);

        Shape circleObject = new Circle(size, userColorMap.get(human.getUserLogin()));
        circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
        circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(human.getCoordinates().getX()));
        circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(human.getCoordinates().getY()));

        circleObject.setOpacity(0.5);

        Text textObject = new Text(Integer.toString(human.getId()));
        textObject.setOnMouseClicked(circleObject::fireEvent);
        textObject.setFont(Font.font(size / 3));
        textObject.setFill(userColorMap.get(human.getUserLogin()).darker());
        textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
        textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

        canvasPane.getChildren().add(circleObject);
        canvasPane.getChildren().add(textObject);
        shapeMap.put(circleObject, human.getId());
        textMap.put(human.getId(), textObject);

        ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
        ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
        circleAnimation.setFromX(0);
        circleAnimation.setToX(1);
        circleAnimation.setFromY(0);
        circleAnimation.setToY(1);
        textAnimation.setFromX(0);
        textAnimation.setToX(1);
        textAnimation.setFromY(0);
        textAnimation.setToY(1);
        circleAnimation.play();
        textAnimation.play();
    }

    /**
     * Shape on mouse clicked.
     */

    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        long id = shapeMap.get(shape);
        for (HumanBeing worker : humanTable.getItems()) {
            if (worker.getId() == id) {

                if(shapeTooltip!=null && shapeTooltip.isShowing()) shapeTooltip.hide();
                if(event.getButton()== MouseButton.SECONDARY) {
                    shapeTooltip = new Tooltip(worker.toString());
                    shapeTooltip.setAutoHide(true);
                    shapeTooltip.show(shape, event.getScreenX(), event.getScreenY());
                }
                humanTable.getSelectionModel().select(worker);
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
        humanTable.setItems(client.getHumanManager().getCollection());
        client.getHumanManager().setController(this);
        client.setResourceFactory(resourceFactory);
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        for (String localeName : localeMap.keySet()) {
            if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                languageComboBox.getSelectionModel().select(localeName);
        }
        if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty()){
            if(localeMap.containsValue(Locale.getDefault())) languageComboBox.getSelectionModel().select(MapUtils.getKeyByValue(localeMap,Locale.getDefault()));
            else languageComboBox.getSelectionModel().selectFirst();
        }

        languageComboBox.setOnAction((event) ->{
            Locale locale = localeMap.get(languageComboBox.getValue());
            resourceFactory.setResources(ResourceBundle.getBundle
                    (App.BUNDLE, locale));
            DateConverter.setPattern(resourceFactory.getRawString("DateFormat"));
            humanTable.refresh();
        });
        bindGuiLanguage();
    }
    public void setApp(App a){
        app = a;
    }
}
