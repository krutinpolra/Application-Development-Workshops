/*
Workshop: 1
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility;

public abstract class CommunicationDevice extends ElectronicDevice implements IDeviceMaintainable, IDeviceOperable {
    public CommunicationDevice(String name, double price, String functionality, String functionType) {
        super(name, price, functionality, functionType);
    }

    @Override
    public abstract String getMaintenanceInstructions();

    @Override
    public abstract String getOperationInstruction();
}
