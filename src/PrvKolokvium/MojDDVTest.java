package PrvKolokvium;
import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

enum DDV_TYPE {
    A,
    B,
    V
}

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",sum));
    }
}

class Proizvod {
    int price;
    DDV_TYPE type;

    public Proizvod() {

    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(DDV_TYPE type) {
        this.type = type;
    }

    public double taxReturn() {
        if (type == DDV_TYPE.A){
            return price * 0.18 * 0.15;
        }
        if (type == DDV_TYPE.B) {
            return price * 0.05 * 0.15;
        }
        return 0;
    }
}

class Smetka {

    String id;
    List<Proizvod> proizvodList;

    public Smetka( String id,List<Proizvod> proizvodList) {
        this.id = id;
        this.proizvodList = proizvodList;
    }
    //12334 1789 А 1238 B 1222 V 111 V
    public static Smetka create(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Proizvod> proizvodi = new ArrayList<>();
        Proizvod proizvod = new Proizvod();

        for (int i = 1; i < parts.length; i++) {
            if (i % 2 == 0) {
                proizvod.setType(DDV_TYPE.valueOf(parts[i]));
                proizvodi.add(proizvod);
                proizvod = new Proizvod();
            } else {
                proizvod.setPrice(Integer.parseInt(parts[i]));
            }
        }
        int sum = proizvodi.stream().mapToInt(line2 -> line2.price).sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }

        return new Smetka(id,proizvodi);
    }

    public double getTaxReturn() {
        return proizvodList.stream().mapToDouble(Proizvod::taxReturn).sum();
    }

    public int sumProizvodi() {
        return proizvodList.stream().mapToInt(line2 -> line2.price).sum();
    }

    //107228 27153 190.09
    @Override
    public String toString() {
        return String.format("%10s\t%10d\t %9.5f",id,sumProizvodi(),getTaxReturn());
    }
}

class MojDDV {
    List<Smetka> smetki;

    public MojDDV() {
        smetki = new ArrayList<>();
    }

    public void readRecords(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        smetki = br.lines().map(line -> {
            try {
                return Smetka.create(line);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void printTaxReturns(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        smetki.forEach(smetka -> pw.println(smetka.toString()));
        pw.flush();
    }

    public void printStatistics(PrintStream out) {
        DoubleSummaryStatistics dss = smetki.stream().mapToDouble(Smetka::getTaxReturn).summaryStatistics();
        //min: MIN max: MAX sum: SUM count: COUNT average: AVERAGE
        String formattedString = String.format("min:\t%.3f\nmax:\t%.3f\nsum:\t%.3f\ncount:\t%d\navg:\t%.3f",dss.getMin(),dss.getMax(),dss.getSum(),dss.getCount(),dss.getAverage());
        out.println(formattedString);
    }
}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}