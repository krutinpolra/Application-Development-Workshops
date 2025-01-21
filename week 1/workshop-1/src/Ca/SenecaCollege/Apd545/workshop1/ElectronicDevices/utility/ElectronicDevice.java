/*
Workshop: 1
Name: KRUTIN BHARATBHAI POLRA
Id: 135416220
*/

package Ca.SenecaCollege.Apd545.workshop1.ElectronicDevices.utility;

public abstract class ElectronicDevice implements Comparable<ElectronicDevice>{
    protected String name;
    protected Double Cost;
    protected String functionality;
    protected String functionType;

    public ElectronicDevice(String name, double Cost, String functionality, String functionType){
        this.name = name;
        this.Cost = Cost;
        this.functionality = functionality;
        this.functionType = functionType;
    }

    public String getName(){
        return name;
    }

    public double getCost(){
        return Cost;
    }

    public String getFunctionality(){
        return functionality;
    }

    public String getFunctionType(){
        return functionType;
    }

    @Override
    public int compareTo(ElectronicDevice other) {
        return Double.compare(this.Cost, other.Cost);
    }
}
