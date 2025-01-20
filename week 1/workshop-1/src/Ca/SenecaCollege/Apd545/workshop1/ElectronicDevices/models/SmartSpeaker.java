package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.UtilityDevice;

public class SmartSpeaker extends UtilityDevice{
    public SmartSpeaker(double price) {
        super("SmartSpeaker", price, "Audio assistance");
    }

    @Override
    public String getMaintenanceInstructions() {
        return "Clean exterior, update firmware";
    }

    @Override
    public String getOperationInstruction() {
        return "By using voice command";
    }
}
