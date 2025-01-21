/*
Workshop: 1
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.models;

import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.Helper;
import Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility.UtilityDevice;

public class SmartSpeaker extends UtilityDevice{

    private Helper maintainer;

    public SmartSpeaker(double price) {
        super("SmartSpeaker", price, "Voice-controlled assistance", "Audio assistance");
        this.maintainer = new Helper() {
            @Override
            public String getMaintenanceInstructions() {
                return "Clean exterior, update firmware";
            }
        };
    }

    @Override
    public String getOperationInstruction() {
        return "By using voice command";
    }

    public String getMaintenanceInstruction (){
        return maintainer.getMaintenanceInstructions();
    }
}
