package interfaces;
import java.io.Serializable;
import exceptions.*;

public interface Vehicle extends Serializable {
    public String getBrand();
    public void setBrand(String newBrand);
    public int getModelCount();

    public String[] getModelNames();
    public double[] getModelPrices();
    public double getPrice(String modelName)
        throws NoSuchModelNameException;
    public void setPrice(String modelName, double newPrice)
        throws NoSuchModelNameException;
    
    public void addModel(String name, double price)
        throws DuplicateModelNameException;
    public void deleteModel(String name)
        throws NoSuchModelNameException;
    public void setModelName(String oldName, String newName)
        throws NoSuchModelNameException, DuplicateModelNameException;
}
