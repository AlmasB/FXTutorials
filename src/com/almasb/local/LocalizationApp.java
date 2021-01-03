package com.almasb.local;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class LocalizationApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        LocalizationService service = new LocalizationService(Locale.ENGLISH);

        Map<String, String> en = new HashMap<>();
        en.put("hello", "Hello World");

        Map<String, String> fr = new HashMap<>();
        fr.put("hello", "Hello World in French");

        Map<String, String> de = new HashMap<>();
        de.put("hello", "Hello World in German");

        service.addLocaleData(Locale.ENGLISH, en);
        service.addLocaleData(Locale.FRENCH, fr);
        service.addLocaleData(Locale.GERMAN, de);

        VBox box = new VBox(15);
        box.setPrefSize(800, 600);

        ChoiceBox<Locale> cbLocale = new ChoiceBox<>(
                FXCollections.observableArrayList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN)
        );

        service.selectedLocale.bind(cbLocale.valueProperty());

        cbLocale.getSelectionModel().selectFirst();

        Text text = new Text();
        text.textProperty().bind(service.localizedStringBinding("hello"));
        text.setFont(Font.font(48));

        box.getChildren().addAll(cbLocale, text);

        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class LocalizationService {

        private java.util.Map<Locale, Translation> localizationData = new HashMap<>();
        private ObjectProperty<Locale> selectedLocale;

        LocalizationService(Locale locale) {
            selectedLocale = new SimpleObjectProperty<>(locale);
        }

        public void addLocaleData(Locale locale, ResourceBundle bundle) {

        }

        public void addLocaleData(Locale locale, java.util.Map<String, String> data) {
            localizationData.put(locale, new MapTranslation(data));
        }

        public StringBinding localizedStringBinding(String key) {
            return Bindings.createStringBinding(() -> getLocalizedString(key, selectedLocale.get()), selectedLocale);
        }

        // TODO: what if locale does not exist in localizationData
        public String getLocalizedString(String key, Locale locale) {
            return localizationData.get(locale).translate(key);
        }
    }

    private interface Translation {
        String translate(String key);
    }

    private static class MapTranslation implements Translation {

        private Map<String, String> data;

        MapTranslation(Map<String, String> data) {
            this.data = data;
        }

        @Override
        public String translate(String key) {
            return data.getOrDefault(key, "NO_TRANSLATION");
        }
    }
}
