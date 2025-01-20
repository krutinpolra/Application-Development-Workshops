/**********************************************
 Workshop 1
 Course: APD545 - Semester: 5th
 Last Name: POLRA
 First Name: KRUTIN
 ID: 135416220
 Section: NBB
 This assignment represents my own work in accordance with Seneca Academic Policy.
 Signature
 Date: January 21st, 2025
 **********************************************/

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