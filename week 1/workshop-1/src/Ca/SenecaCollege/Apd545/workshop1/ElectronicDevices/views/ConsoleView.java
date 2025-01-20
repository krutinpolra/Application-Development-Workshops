package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.views;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models.*;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.CommunicationDevice;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.ElectronicDevice;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.EntertainmentDevice;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.UtilityDevice;

import java.util.Scanner;

public class ConsoleView {

    private Scanner input;

    public ConsoleView() {
        input = new Scanner(System.in);
    }

    public void displayMessage (String message){
        System.out.println(message);
    }

    public double getCostInput(String prompt){
        System.out.print(prompt);
        return input.nextDouble();
    }

    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return input.next();
    }

    public void displayDeviceDetails(ElectronicDevice device){
        displayMessage("--: Requirement 2 :--");
        displayMessage("The most expensive device is: " + device.getName());
        displayMessage(device.getName() + "/' cost is: $" + device.getCost());
        displayMessage(device.getName() + " is operated: " + device.getOperationInstruction());
        displayMessage(device.getName() + " maintenance: " + device.getMaintenanceInstructions());
        displayMessage(device.getName() + " function type: " + device.getFunctionType());
        displayMessage("\n");
    }

    public void displayInDescendingOrder(ElectronicDevice[] devices){
        displayMessage("--: Requirement 3 :--");
        displayMessage("Devices in Descending Order of Price:");
        for (ElectronicDevice device : devices){
            displayMessage(device.getName());
        }
        displayMessage("\n");
    }

    public void displayDevicesByCategory(ElectronicDevice[] devices){

        displayMessage("--: Requirement 4 :--");
        String category = getStringInput("Enter a device category (CommunicationDevice, EntertainmentDevice, UtilityDevice): ");
        for (ElectronicDevice device : devices) {
            if ((category.equalsIgnoreCase("CommunicationDevice") && device instanceof CommunicationDevice) ||
                    (category.equalsIgnoreCase("EntertainmentDeice") && device instanceof EntertainmentDevice) ||
                    (category.equalsIgnoreCase("UtilityDevices") && device instanceof UtilityDevice)) {
                displayMessage(device.getName() + ": " + device.getFunctionality());
            }
        }
        displayMessage("\n");
    }

    public void closeScanner() {
        input.close();
    }
}
