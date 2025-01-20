package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility;

public abstract class ElectronicDevice implements IDeviceMaintainable, IDeviceOperable, Comparable<ElectronicDevice> {
    protected String name;
    protected Double Cost;
    protected String functionType;

    public ElectronicDevice(String name, double Cost, String functionType){
        this.name = name;
        this.Cost = Cost;
        this.functionType = functionType;
    }

    public String getName(){
        return name;
    }

    public double getCost(){
        return Cost;
    }

    public String getFunctionType(){
        return functionType;
    }

    @Override
    public int compareTo(ElectronicDevice o) {
        return Double.compare(o.getCost(), this.Cost);
    }

    @Override
    public String toString() {
        return name;
    }
}
