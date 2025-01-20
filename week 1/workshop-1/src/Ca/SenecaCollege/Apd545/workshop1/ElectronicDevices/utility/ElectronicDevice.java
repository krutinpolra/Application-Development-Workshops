package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility;

public abstract class ElectronicDevice implements IDeviceMaintainable, IDeviceOperatable, Comparable<ElectronicDevice> {
    protected String name;
    protected Double price;
    protected String functionType;

    public ElectronicDevice(String name, double price, String functionType){
        this.name = name;
        this.price = price;
        this.functionType = functionType;
    }

    public String getName(){
        return name;
    }

    public double getCost(){
        return price;
    }

    public String getFunctionType(){
        return functionType;
    }

}
