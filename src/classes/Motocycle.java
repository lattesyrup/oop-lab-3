package classes;
import interfaces.*;
import exceptions.*;
import java.io.Serializable;

public class Motocycle implements Vehicle {
    private static final long serialVersionUID = 1L;

    private class Model implements Serializable {
        private String name = null;
        private double price = Double.NaN;
    
        // cycled double linked list architecture //
        private Model prev;
        private Model next;

        public Model() { }

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
    
    private String motoBrand;
    private int count = 0;
    private Model head;
    
    private transient long lastModified;
    // shouldn't be serialized: while deserializing object
    // this field takes creation time as it should
    
    {
        head = new Model();
        head.next = head;
        head.prev = head;

        updateLastModified();
    }

    public Motocycle(String motoBrand, int modelCount) {
        this.motoBrand = motoBrand;

        for (int i = 1; i <= modelCount; i++)
            insertElementAfterLast( new Model(
                "Basic " + i,
                (int)(Math.random() * 3) * 25 + 50)
            ); // 50 or 75 or 100
    }

    public Motocycle(String motoBrand) {
        this(motoBrand, 0);
    }

    private void updateLastModified() {
        lastModified = System.currentTimeMillis();
    }

    private void insertElementAfterLast(Model insertModel) {
        insertModel.prev = head.prev;
        insertModel.next = head;
        head.prev.next = insertModel;
        head.prev = insertModel;
        count++;
    }

    @Override
    public String getBrand() {
        return motoBrand;
    }
    
    @Override
    public void setBrand(String newMotoBrand) {
        motoBrand = newMotoBrand;
        updateLastModified();
    }

    @Override
    public void setModelName(String oldName, String newName)
    throws NoSuchModelNameException, DuplicateModelNameException {
        Model temp = head.next,
            find = null;
        while (temp != head) {
            if (temp.name.equals(newName))
                throw new DuplicateModelNameException(
                    "Cannot rename " + oldName + " to " + newName + ": already exists");
            
            if (temp.name.equals(oldName))
                find = temp;

            temp = temp.next;
        }

        if (find == null)
            throw new NoSuchModelNameException(
                "Model with name " + oldName + " not found");
        
        find.name = newName;
    }

    @Override
    public String[] getModelNames() {
        String[] result = new String[count];
        Model link = head.next;

        for (int i = 0; link != head; i++) {
            result[i] = link.name;
            link = link.next;
        }
        return result;
    }

    @Override
    public double[] getModelPrices() {
        double[] result = new double[count];
        Model link = head.next;

        for (int i = 0; link != head; i++) {
            result[i] = link.price;
            link = link.next;
        }
        return result;
    }

    @Override
    public double getPrice(String modelName)
    throws NoSuchModelNameException {
        Model link = head.next;
        while (link != head) {
            if (link.name.equals(modelName)) return link.price;
            link = link.next;
        }

        throw new NoSuchModelNameException(
            "Model with name " + modelName + " not found");
    }

    @Override
    public void setPrice(String modelName, double newPrice)
    throws NoSuchModelNameException {
        if (newPrice <= 0)
            throw new ModelPriceOutOfBoundsException(
                "Incorrect price: $" + newPrice + " for model " + modelName);
      
        Model link = head.next;
        while (link != head) {
            if (link.name.equals(modelName)) {
                link.price = newPrice;
                updateLastModified();
                return;
            }
            link = link.next;
        }
        throw new NoSuchModelNameException(
            "Model with name " + modelName + " not found");
    }

    @Override
    public void addModel(String name, double price)
    throws DuplicateModelNameException {
        if (price <= 0)
            throw new ModelPriceOutOfBoundsException(
                "Incorrect price: $" + price + " for model " + name);

        Model link = head.next;
        while (link != head) {
            if (link.name.equals(name))
                throw new DuplicateModelNameException(
                    "Cannot create " + name + ": already exists");
            link = link.next;
        }
        insertElementAfterLast( new Model(name, price) );
        updateLastModified();
    }

    @Override
    public void deleteModel(String name)
    throws NoSuchModelNameException {
        Model link = head.next;
        while (link != head) {
            if (link.name.equals(name)) {
                link.next.prev = link.prev;
                link.prev.next = link.next;
                count--;

                updateLastModified();
                return;
            }
            link = link.next;
        }

        throw new NoSuchModelNameException(
            "Model with name " + name + " not found");
    }

    @Override
    public int getModelCount() {
        return count;
    }

    public long getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Motocycle m = (Motocycle) o;
        boolean equals = 
            motoBrand.equals(m.getBrand()) &&
            count == m.getModelCount();
        if (equals) {
            Model
                t1 = head.next,
                t2 = m.head.next;
            while (t1 != head && t2 != m.head && equals) {
                equals = t1.equals(t2);
                t1 = t1.next;
                t2 = t2.next;
            }
        }

        return equals;
    }
}
