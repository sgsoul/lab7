package collection;

import common.collection.HumanManagerImpl;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.data.HumanBeing;
import common.exceptions.NoSuchIdException;
import controllers.MainWindowController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HumanObservableManager extends HumanManagerImpl<ObservableList<HumanBeing>> {
    private ObservableList<HumanBeing> collection;
    private Set<Integer> uniqueIds;
    private MainWindowController controller;

    public HumanObservableManager() {
        collection = FXCollections.observableArrayList();
        uniqueIds = ConcurrentHashMap.newKeySet();
    }

    public Set<Integer> getUniqueIds() {
        return uniqueIds;
    }

    public void applyChanges(Response response) {
        CollectionOperation op = response.getCollectionOperation();
        Collection<HumanBeing> changes = response.getCollection();
        ObservableList<HumanBeing> old = FXCollections.observableArrayList(collection);

        if (op == CollectionOperation.ADD) {
            for (HumanBeing human : changes) {
                super.addWithoutIdGeneration(human);
            }
        }

        if (op == CollectionOperation.REMOVE) {
            for (HumanBeing human : changes) {
                Collections.copy(old, collection);
                super.removeByID(human.getId());
            }
        }

        if (op == CollectionOperation.UPDATE) {
            for (HumanBeing human : changes) {
                super.updateByID(human.getId(), human);
            }
        }

        if (controller != null && op != CollectionOperation.NONE && changes != null && !changes.isEmpty()) {
            Platform.runLater(() -> {

                        controller.refreshCanvas(op != CollectionOperation.REMOVE ? collection : old, changes, op);
                        controller.refreshTable();
                    }
            );
        }

    }

    public ObservableList<HumanBeing> getCollection() {
        return collection;
    }

    @Override
    public void updateByID(Integer id, HumanBeing newHuman) {
        assertNotEmpty();
        Optional<HumanBeing> human = getCollection().stream()
                .filter(h -> h.getId() == id)
                .findFirst();
        if (!human.isPresent()) {
            throw new NoSuchIdException(id);
        }
        Collections.replaceAll(collection, human.get(), newHuman);
    }

    public void setController(MainWindowController c) {
        controller = c;
    }

    public MainWindowController getController() {
        return controller;
    }
}

