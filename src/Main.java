import java.io.*;
import classes.*;
import interfaces.Vehicle;
import utils.VehicleUtils;

public class Main {
    public static void main(String[] args) {
        Vehicle v = null;
        exceptionTest(v);
        System.out.println();

        v = new Motocycle("BMW", 3);
        String
            binFilename = "./target/test.bin",
            txtFilename = "./target/test.txt",
            serFilename = "./target/test.ser";

        inputOutputTest(v, binFilename);
        System.out.println();

        v = new Auto("Renault", 4);
        writeReadTest(v, txtFilename);
        System.out.println();

        v = new Auto("Mercedes", 2);
        consoleInOutTest(v);
        System.out.println();

        v = new Motocycle("Yamaha", 6);
        serializationTest(v, serFilename);
        System.out.println();
    }
    
    private static void exceptionTest(Vehicle v) {
        try {
            v = new Auto("BMW", 4);
            VehicleUtils.printVehicleInfo(v);
            
            Logger.log("renaming to Yamaha...");
            v.setBrand("Yamaha");
    
            Logger.log("adding TW200 with price $142...");
            v.addModel("TW200", 142);
            
            Logger.log("getting price of PW50...");
            v.getPrice("PW50");
            
            Logger.log("setting price of Basic 2 to $-35...");
            v.setPrice("Basic 2", -35);
            
            Logger.log("deleting Basic 1...");
            v.deleteModel("Basic 1");
            
            VehicleUtils.printVehicleInfo(v);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputOutputTest(Vehicle v, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            VehicleUtils.outputVehicle(v, fos);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try (FileInputStream fis = new FileInputStream(filename)) {
            Vehicle v2 = VehicleUtils.inputVehicle(fis);

            VehicleUtils.printVehicleInfo(v);
            VehicleUtils.printVehicleInfo(v2);

            System.out.println(
                "i/o test: v2 is " +
                ((v.equals(v2)) ? "" : "NOT ") +
                "equal to v"
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void writeReadTest(Vehicle v, String filename) {
        try (FileWriter fw = new FileWriter(filename)) {
            VehicleUtils.writeVehicle(v, fw);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try (FileReader fr = new FileReader(filename)) {
            Vehicle v2 = VehicleUtils.readVehicle(fr);

            VehicleUtils.printVehicleInfo(v);
            VehicleUtils.printVehicleInfo(v2);

            System.out.println(
                "write/read test: v2 is " +
                ((v.equals(v2)) ? "" : "NOT ") +
                "equal to v"
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void consoleInOutTest(Vehicle v) {
        try {
            Logger.log("read: system.out");
            VehicleUtils.writeVehicle(v, new OutputStreamWriter(System.out));

            System.out.println("for testing equality, type exactly what you see.\n");
            Logger.log("write: system.in");
            Vehicle v2 = VehicleUtils.readVehicle(new InputStreamReader(System.in));
            if (v2 != null)
            {
                VehicleUtils.printVehicleInfo(v);
                VehicleUtils.printVehicleInfo(v2);

                System.out.println(
                    "console test: v2 is " +
                    ((v.equals(v2)) ? "" : "NOT ") +
                    "equal to v"
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void serializationTest(Vehicle v, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(v);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Vehicle v2 = (Vehicle)ois.readObject();

            VehicleUtils.printVehicleInfo(v);
            VehicleUtils.printVehicleInfo(v2);

            System.out.println(
                "serialization test: v2 is " +
                ((v.equals(v2)) ? "" : "NOT ") +
                "equal to v"
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}