import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        String str = "Hello";
        String substring = next.substring(next.indexOf('.'), next.indexOf('/'));
        System.out.println(substring);
        System.out.println(next);
    }
}
