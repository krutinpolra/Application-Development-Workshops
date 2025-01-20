package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.tester;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.controllers.DeviceController;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.views.ConsoleView;

public class Main {
    public static void main(String[] args) {

        ConsoleView view = new ConsoleView();
        DeviceController controller = new DeviceController(view);

        controller.initializeDevice();
        controller.mostExpensiveDevice();
        controller.sortDeviceByPrice();
        controller.sortDeviceByCategory();

        view.closeScanner();


    }
}