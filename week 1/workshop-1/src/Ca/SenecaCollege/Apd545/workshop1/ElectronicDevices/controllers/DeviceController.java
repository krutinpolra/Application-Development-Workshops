/*
Workshop: 1
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/
package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.controllers;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models.*;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.ElectronicDevice;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.views.ConsoleView;

import java.util.Arrays;
import java.util.Comparator;

public class DeviceController {
    private ConsoleView view;
    private ElectronicDevice[] devices;

    public DeviceController(ConsoleView view) {
        this.view = view;
        devices = new ElectronicDevice[5];
    }

    public void initializeDevice(){
        view.displayMessage("--: Requirement 1 :--");
        devices[0] = new SmartPhone(view.getCostInput("Enter the price for SmartPhone: "));
        devices[1] = new Tablet(view.getCostInput("Enter price of Tablet: "));
        devices[2] = new GamingConsole(view.getCostInput("Enter the price of GamingConsole: "));
        devices[3] = new SmartTv(view.getCostInput("Enter the price for SmartTV: "));
        devices[4] = new SmartSpeaker(view.getCostInput("Enter the price for SmartSpeaker: "));
        view.displayMessage("\n");
    }

    public void mostExpensiveDevice() {
        ElectronicDevice mostExpensive = devices[0];
        for(ElectronicDevice device : devices){
            if (device.getCost() > mostExpensive.getCost()){
                mostExpensive = device;
            }
        }
        view.displayDeviceDetails(mostExpensive);
    }

    public void sortDeviceByPrice() {
        Arrays.sort(devices, Comparator.reverseOrder());
        view.displayInDescendingOrder(devices);
    }

    public void sortDeviceByCategory() {
        view.displayDevicesByCategory(devices);
    }
}
