package Aud3;

interface Operation{
    //lambdas can be used ONLY IF there is only one method in the interface
    //functional interfaces
    double execute(double a, double b);
}
public class LambdaIntro {
    public static void main(String[] args) {

        Operation addition = (a,b) -> a+b;
        Operation subtraction = (a,b) -> a-b;
        Operation multiplication = (a,b) -> a*b;
        Operation division = (a,b) -> a/b;

        float a = 5, b = 8;

        System.out.println(addition.execute(a,b));
        System.out.println(subtraction.execute(a,b));
        System.out.println(multiplication.execute(a,b));
        System.out.println(division.execute(a,b));

    }
}
