/*
Workshop: 1
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.CommunicationDevice;
public class Tablet extends CommunicationDevice{
    public Tablet(double Cost) {
        super("Tablet", Cost, "Larger screen communication", "Multi-functional");
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
