package pl.markiewicz.teai_4.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.markiewicz.teai_4.Car;
import pl.markiewicz.teai_4.CarService;

import javax.management.Notification;

@Route("vaadin")
public class CarGui extends VerticalLayout {

    private CarService carService;

    @Autowired
    public CarGui(CarService carService) {
        this.carService = carService;

        Grid<Car> carGrid = new Grid<>();
        carGrid.setItems(carService.getCarList());

        carGrid.setWidth("700px");
        carGrid.setHeight("600px");
        carGrid.setVerticalScrollingEnabled(true);
        setHorizontalComponentAlignment(Alignment.CENTER, carGrid);

        Grid.Column<Car> idColumn = carGrid.addColumn(Car::getId).setHeader("id");
        Grid.Column<Car> markColumn = carGrid.addColumn(Car::getMark).setHeader("Mark");
        Grid.Column<Car> modelColumn = carGrid.addColumn(Car::getModel).setHeader("Model");
        Grid.Column<Car> colorColumn = carGrid.addColumn(Car::getColor).setHeader("Color");

        carGrid.addComponentColumn(item -> createRemoveButton(carGrid, item)).setWidth("20px").setHeader("");

        carGrid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        ListDataProvider<Car> dataProvider = new ListDataProvider<>(carService.getCarList());
        carGrid.setDataProvider(dataProvider);

        HeaderRow filterRow = carGrid.appendHeaderRow();
        FooterRow footerRow = carGrid.appendFooterRow();

        TextField idFieldFilter = new TextField();
        idFieldFilter.addValueChangeListener(event -> dataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(Long.toString(car.getId()),
                        idFieldFilter.getValue())));

        idFieldFilter.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(idColumn).setComponent(idFieldFilter);
        idFieldFilter.setSizeFull();
        idFieldFilter.setPlaceholder("id filter");

        TextField idField = new TextField();
        footerRow.getCell(idColumn).setComponent(idField);
        idField.setSizeFull();
        idField.setPlaceholder("new id");

        TextField markField = new TextField();
        footerRow.getCell(markColumn).setComponent(markField);
        markField.setSizeFull();
        markField.setPlaceholder("new mark");

        TextField modelField = new TextField();
        footerRow.getCell(modelColumn).setComponent(modelField);
        modelField.setSizeFull();
        modelField.setPlaceholder("new model");

        TextField colorField = new TextField();
        footerRow.getCell(colorColumn).setComponent(colorField);
        colorField.setSizeFull();
        colorField.setPlaceholder("new color");

        FooterRow addRow = carGrid.appendFooterRow();
        Button addButton = new Button(new Icon(VaadinIcon.PLUS));
        addRow.getCell(colorColumn).setComponent(addButton);

        addButton.addClickListener(buttonClickEvent -> {
            Car car = new Car(Long.parseLong(idField.getValue()),markField.getValue(),
                    modelField.getValue(),colorField.getValue());
            carService.addCar(car);
            dataProvider.refreshAll();
        });

        add(carGrid);
    }

    private Button createRemoveButton(Grid<Car> carGrid, Car item) {
        @SuppressWarnings("unchecked")
        Button button = new Button(new Icon(VaadinIcon.CLOSE), clickEvent -> {
            ListDataProvider<Car> dataProvider = (ListDataProvider<Car>) carGrid.getDataProvider();
            dataProvider.getItems().remove(item);
            dataProvider.refreshAll();
        });
        return button;
    }
}
