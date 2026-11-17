public class ObjectReferences {

    static final class Pet {
        private String name;

        Pet(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void rename(String newName) {
            name = newName;
        }
    }

    public static void main(String[] args) {
        Pet first = new Pet("Luna");
        Pet alias = first;
        Pet independent = new Pet("Luna");

        System.out.println("First and alias: " + (first == alias));
        System.out.println("First and independent: " + (first == independent));

        alias.rename("Sol");

        System.out.println("First name: " + first.getName());
        System.out.println("Independent name: " + independent.getName());

        String firstText = new String("Luna");
        String secondText = new String("Luna");
            
        System.out.println(firstText == secondText);
        System.out.println(firstText.equals(secondText));

        int[] firstNumbers = {2, 4, 6};
        int[] otherArray = {2, 4, 6};
        int[] sameArray = firstNumbers;

        
        sameArray[0] = 99;
        System.out.println(otherArray[0]);
        System.out.println(firstNumbers[0]);
        System.out.println(firstNumbers == otherArray);
        System.out.println(firstNumbers == sameArray);


        Pet current = new Pet("Luna");
        current = new Pet("Sol");
    }
}