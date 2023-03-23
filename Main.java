public class Main {
    public static void main(String[] args) {
        WordManager manager = new WordManager(args);
        System.out.println(manager);
        for (short i = 0; i<100; i++) {
            System.out.println(manager.getWord());
        }
    }
}