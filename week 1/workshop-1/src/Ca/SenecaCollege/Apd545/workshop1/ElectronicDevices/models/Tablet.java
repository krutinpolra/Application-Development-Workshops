package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.CommunicationDevice;
public class Tablet extends CommunicationDevice{
    public Tablet(double Cost) {
        super("Tablet", Cost, "Multi-functional");
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
