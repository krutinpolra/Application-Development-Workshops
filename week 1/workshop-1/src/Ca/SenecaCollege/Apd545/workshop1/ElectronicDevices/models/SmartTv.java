/*
Workshop: 1
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.EntertainmentDevice;

public class SmartTv extends EntertainmentDevice {
    public SmartTv(double price) {
        super("SmartTV", price, "Streaming and media viewing", "Visual entertainment");
    }

    @Override
    public String getMaintenanceInstructions() {
        return "Update firmware, clean screen";
    }

    @Override
    public String getOperationInstruction() {
        return "By using remote control";
    }
}
