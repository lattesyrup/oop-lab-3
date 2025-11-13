package utils;
import java.io.*;
import interfaces.*;
import classes.*;
import exceptions.NoSuchVehicleTypeException;

public class VehicleUtils {
    public static double getAveragePrice(Vehicle v) {
        double[] prices = v.getModelPrices();
        double sum = 0;
        for (double p : prices)
            sum += p;
        return sum / prices.length;
    }

    public static void printVehicleInfo(Vehicle v) {
        String[] names = v.getModelNames();
        double[] prices = v.getModelPrices();

        System.out.println("---\t" + v.getClass().getSimpleName() + " " + v.getBrand() + "\t---");
        System.out.println("Models (count: " + v.getModelCount() + "):");
        for (int i = 0; i < names.length; i++)
            System.out.println((i + 1) + ") " + names[i] + ":\t$" + prices[i]);
        System.out.println("---\t---\t---\t---");
    }

    // external work methods //

    public static void outputVehicle(Vehicle v, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            dos.writeInt(v.getClass().getSimpleName().length());
            dos.write(v.getClass().getSimpleName().getBytes());
            dos.writeInt(v.getBrand().length());
            dos.write(v.getBrand().getBytes());

            // Logger.log("output: wrote vehicle " + v.getBrand() + " to output...");

            String[] names = v.getModelNames();
            double[] prices = v.getModelPrices();
            dos.writeInt(v.getModelCount());

            for (int i = 0; i < v.getModelCount(); i++)
            {
                dos.writeInt(names[i].length());
                dos.writeBytes(names[i]);
                dos.writeDouble(prices[i]);
                // Logger.log("added " + names[i] + " with price $" + prices[i] + "...");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Vehicle inputVehicle(InputStream in)
    throws NoSuchVehicleTypeException {
        Vehicle v = null;

        try (DataInputStream dis = new DataInputStream(in)) {
            String type = new String(dis.readNBytes(dis.readInt()));
            String brand = new String(dis.readNBytes(dis.readInt()));
            v = createFromType(type, brand);
            // Logger.log("input: created " + v.getClass().getSimpleName() + " " + brand + " from input...");

            String name;
            double price;
            int modelCount = dis.readInt();
            // Logger.log("input: trying to create " + modelCount + " models...");

            for (int i = 0; i < modelCount; i++) {
                name = new String(dis.readNBytes(dis.readInt()));
                price = dis.readDouble();
                v.addModel(name, price);
                // Logger.log("added " + name + " with price $" + price + "...");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return v;
    }

    public static void writeVehicle(Vehicle v, Writer out) {
        try {
            PrintWriter pw = new PrintWriter(out);

            pw.println(v.getClass().getSimpleName());
            pw.println(v.getBrand());
            pw.println(v.getModelCount());

            String[] names = v.getModelNames();
            double[] prices = v.getModelPrices();

            for (int i = 0; i < v.getModelCount(); i++) {
                pw.println(names[i] + ": $" + prices[i]);
                // Logger.log("added " + names[i] + " with price $" + prices[i] + "...");
            }

            pw.flush();

            // Logger.log("writer: wrote vehicle " + v.getBrand() + " to output...");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Vehicle readVehicle(Reader in) { 
        Vehicle v = null;

        try (BufferedReader br = new BufferedReader(in)) {
            String type = br.readLine();
            String brand = br.readLine();
            v = createFromType(type, brand);
            Logger.log("reader: created " + v.getClass().getSimpleName() + " " + brand + " from input...");

            int modelCount = Integer.parseInt(br.readLine());
            Logger.log("reader: trying to create " + modelCount + " models...");
            Logger.log("reminder: model typing format is *exactly* \"<name>: $<price>\".");

            for (int i = 0; i < modelCount; i++) {
                String[] line = br.readLine().split(": \\$");
                String name = line[0];
                double price = Double.parseDouble(line[1]);
                v.addModel(name, price);
                Logger.log("added " + name + " with price $" + price + "...");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return v;
    }

    private static Vehicle createFromType(String type, String brand)
    throws NoSuchVehicleTypeException {
        Vehicle result = switch (type) {
            case "Auto": yield new Auto(brand);
            case "Motocycle": yield new Motocycle(brand);
            default: throw new NoSuchVehicleTypeException("Error while reading class " + type);
        };
        return result;
    }
}
