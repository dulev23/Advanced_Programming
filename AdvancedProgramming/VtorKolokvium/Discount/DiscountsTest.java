package VtorKolokvium.Discount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Item {
    private final int price;
    private final double discountPrice;

    public Item(int price, double discountPrice) {
        this.price = price;
        this.discountPrice = discountPrice;
    }

    public int getPrice() {
        return price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public double discount() {
        return getPrice() - getDiscountPrice();
    }

    public double getDiscountPercentage() {
        return Math.floor(discount()/getPrice() * 100);
    }

    @Override
    public String toString() {
        return String.format(" %.1f %d ", getDiscountPrice(), getPrice());
    }
}

class Store implements Comparable<Store> {
    public String storeName;
    public List<Item> itemList;

    public Store(String storeName) {
        this.storeName = storeName;
        itemList = new ArrayList<>();
    }

    public double averageDiscount() {
        return itemList.stream().mapToDouble(Item::getDiscountPercentage).average().orElse(0);
    }

    public double totalDiscount() {
        return itemList.stream().mapToDouble(Item::discount).sum();
    }

    public String getStoreName() {
        return storeName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(storeName).append('\n');
        sb.append(String.format("Average discount: %.1f%%\n", averageDiscount()));
        sb.append(String.format("Total discount: %.0f\n", totalDiscount()));
        itemList.stream()
                .sorted(Comparator.comparingDouble(Item::getDiscountPercentage)
                        .thenComparing(Item::getPrice).reversed())
                .forEach(item -> {
            int discountPercent =(int) item.getDiscountPercentage();
            sb.append(String.format("%2d%% %d/%d\n", discountPercent,
                    (int) item.getDiscountPrice(),
                    item.getPrice()));
        });
        return sb.toString().trim();
    }

    @Override
    public int compareTo(Store o) {
        return Double.compare(this.averageDiscount(), o.averageDiscount());
    }

    public void addItem(Item item) {
        itemList.add(item);
    }
}

class Discounts {
    private List<Store> storeList;

    public int readStores(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        storeList = br.lines().map(line -> {
            String[] parts = line.split("\\s+");
            Store store = new Store(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                String[] itemParts = parts[i].split(":");
                double discountPrice = Double.parseDouble(itemParts[0]);
                int price = Integer.parseInt(itemParts[1]);
                store.addItem(new Item(price, discountPrice));
            }
            return store;
        }).collect(Collectors.toList());
        return storeList.size();
    }

    public List<Store> byAverageDiscount() {
        return storeList.stream()
                .sorted(Comparator.comparingDouble(Store::averageDiscount).reversed()
                        .thenComparing(Store::getStoreName))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return storeList.stream()
                .sorted(Comparator.comparingDouble(Store::totalDiscount)
                        .thenComparing(Store::getStoreName))
                .limit(3)
                .collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}