package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.EntertainmentDevice;

public class GamingConsole extends EntertainmentDevice{
    public GamingConsole(double price) {
        super("GamingConsole", price, "Video gaming", "Interactive entertainment");
    }

    @Override
    public String getMaintenanceInstructions() {
        return "Clean vents, Software updates";
    }

    @Override
    public String getOperationInstruction() {
        return "By using game controllers";
    }
}
