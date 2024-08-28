package VtorKolokvium.OnlinePayments;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

class Item {
    private String studentIndex;
    private String name;
    private int price;

    public Item(String studentIndex, String name, int price) {
        this.studentIndex = studentIndex;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getStudentIndex() {
        return studentIndex;
    }

    public static Item createItem(String line) {
        String[] parts = line.split(";");
        String studentIndex = parts[0];
        String name = parts[1];
        int price = Integer.parseInt(parts[2]);
        return new Item(studentIndex, name, price);
    }
}

class OnlinePayments {
    private Map<String, List<Item>> studentItems;

    public OnlinePayments() {
        studentItems = new HashMap<>();
    }


    public void readItems(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.lines().forEach(line -> {
            Item item = Item.createItem(line);
            String studentIndex = item.getStudentIndex();
            if (!studentItems.containsKey(studentIndex)) {
                studentItems.put(studentIndex, new ArrayList<>());
            }
            studentItems.get(studentIndex).add(item);
        });
    }

    public void printStudentReport(String id, PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        List<Item> items = studentItems.get(id);
        if (items == null || items.isEmpty()) {
            pw.printf("Student %s not found!\n", id);
            pw.flush();
            return;
        }

        items.sort(Comparator.comparingInt(Item::getPrice).reversed());
        double netAmount = items.stream().mapToDouble(Item::getPrice).sum();
        double fee = netAmount * 0.0114;
        fee = Math.round(fee);
        if (fee < 3) fee = 3; //minimum
        if (fee > 300) fee = 300; //maximum

        double totalAmount = netAmount + Math.round(fee);
        pw.printf("Student: %s Net: %.0f Fee: %.0f Total: %.0f \n", id, netAmount, fee, totalAmount);
        pw.println("Items:");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            pw.printf("%d. %s %d\n", i + 1, item.getName(), item.getPrice());
        }

        pw.flush();
    }
}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}