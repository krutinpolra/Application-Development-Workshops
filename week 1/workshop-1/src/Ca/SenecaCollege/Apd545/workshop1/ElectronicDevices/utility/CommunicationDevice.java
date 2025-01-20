package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility;

public abstract class CommunicationDevice extends ElectronicDevice{
    public CommunicationDevice(String name, double price, String functionType) {
        super(name, price, functionType);
    }

    @Override
    public int compareTo(ElectronicDevice other) {
        return Double.compare(other.getCost(), this.price); // Descending order
    }
}
