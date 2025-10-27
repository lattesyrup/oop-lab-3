package classes;
import interfaces.Vehicle;
import exceptions.*;

import java.io.Serializable;
import java.util.Arrays;

public class Auto implements Vehicle {
    private static final long serialVersionUID = 1L;

    private class Model implements Serializable {
        private String name = null;
        private double price;
        
        public Model(String name, double price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;

            Model m = (Model) o;
            return
                name.equals(m.name) &&
                price == m.price;
        }
    }

    private String carBrand;
    private Model[] models;

    public Auto(String carBrand, int count) {
        this.carBrand = carBrand;
        models = new Model[count];
        for (int i = 1; i <= count; i++)
            models[i - 1] = new Model(
                "Basic " + i,
                (int)(Math.random() * 3) * 50 + 100 ); // 100 or 150 or 200
    }

    public Auto(String carBrand) {
        this(carBrand, 0);
    }

    @Override
    public String getBrand() {
        return carBrand;
    }
    
    @Override
    public void setBrand(String newCarBrand) {
        carBrand = newCarBrand;
    }
    
    @Override
    public String[] getModelNames() {
        String[] result = new String[models.length];
        for (int i = 0; i < result.length; i++)
            result[i] = models[i].name;
        return result;
    }
    
    @Override
    public double[] getModelPrices() {
        double[] result = new double[models.length];
        for (int i = 0; i < result.length; i++)
            result[i] = models[i].price;
        return result;
    }
    
    @Override
    public double getPrice(String modelName)
    throws NoSuchModelNameException {
        for (Model m : models)
            if (m.name.equals(modelName))
                return m.price;
        throw new NoSuchModelNameException("Model with name " + modelName + " not found");
    }

    @Override
    public void setPrice(String modelName, double newPrice)
    throws NoSuchModelNameException {
        if (newPrice <= 0)
            throw new ModelPriceOutOfBoundsException(
                "Incorrect price: $" + newPrice + " for model " + modelName);
            
        for (int i = 0; i < models.length; i++)
            if (models[i].name.equals(modelName)) {
                models[i].price = newPrice;
                return;
            }
        
        throw new NoSuchModelNameException(
            "Model with name " + modelName + " not found");
    }

    @Override
    public void setModelName(String oldName, String newName)
    throws NoSuchModelNameException, DuplicateModelNameException {
        int index = -1;

        for (int i = 0; i < models.length; i++) {
            if (models[i].name.equals(newName))
            throw new DuplicateModelNameException(
                "Cannot rename " + oldName + " to " + newName + ": already exists");

            if (models[i].name.equals(oldName)) {
                index = i;
            }
        }

        if (index == -1)
            throw new NoSuchModelNameException(
                "Model with name " + oldName + " not found");

        models[index].name = newName;
    }

    @Override
    public void addModel(String name, double price)
    throws DuplicateModelNameException {
        if (price <= 0)
            throw new ModelPriceOutOfBoundsException(
                "Incorrect price: $" + price + " for model " + name);

        for (int i = 0; i < models.length; i++)
            if (models[i].name.equals(name))
                throw new DuplicateModelNameException(
                    "Cannot create " + name + ": already exists");

        models = Arrays.copyOf(models, models.length + 1);
        models[models.length - 1] = new Model(name, price);
    }

    @Override
    public void deleteModel(String name)
    throws NoSuchModelNameException {
        for (int i = 0; i < models.length; i++) {
            if (models[i].name.equals(name)) {
                System.arraycopy(models, i + 1, models, i, models.length - i - 1);
                models = Arrays.copyOf(models, models.length - 1);
                return;
            }
        }
        throw new NoSuchModelNameException(
            "Model with name " + name + " not found");
    }
    
    @Override
    public int getModelCount() {
        return models.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Auto a = (Auto) o;
        boolean equals = 
            carBrand.equals(a.getBrand()) &&
            getModelCount() == a.getModelCount();
        
        // there was if (equals), but it's not necessary
        for (int i = 0; i < getModelCount() && equals; i++)
            equals = models[i].equals(a.models[i]);

        return equals;
    }
}
