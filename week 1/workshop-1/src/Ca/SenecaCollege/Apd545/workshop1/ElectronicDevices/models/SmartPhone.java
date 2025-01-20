package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.CommunicationDevice;

public class SmartPhone extends CommunicationDevice {
    public SmartPhone(double price) {
        super("SmartPhone", price, "Multi-functional");
    }

    public String getMaintenanceInstructions() {
        return "Regular software updates";
    }

    public String getOperationInstruction() {
        return "By using touchscreen";
    }

}
