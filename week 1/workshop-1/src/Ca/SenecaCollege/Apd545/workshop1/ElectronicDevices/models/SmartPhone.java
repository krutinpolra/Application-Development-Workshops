/*
Workshop: 1
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.CommunicationDevice;

public class SmartPhone extends CommunicationDevice {
    public SmartPhone(double price) {
        super("SmartPhone", price, "Communication and apps", "Multi-functional");
    }

    @Override
    public String getMaintenanceInstructions() {
        return "Regular software updates";
    }

    @Override
    public String getOperationInstruction() {
        return "By using touchscreen";
    }

}
