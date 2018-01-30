// Simple class hierarchy
// Demonstrates abstract classes, containment

public class Animals1 {

    // inner classes
    // base class
    abstract class Animal {
        abstract String talk();
    }

    // derived classes
    class Cat extends Animal {
        String talk() { return "Meow!"; }
    }

    class Dog extends Animal {
        String talk() { return "Woof!"; }
    }

    // container class methods
    Animals1() {
        speak(new Cat());
        speak(new Dog()); 
    }

    void speak(Animal a) {
        System.out.println( a.talk() );
    }

    // static main methods -- entry point
    public static void main(String[] args) {
        Animals1 a = new Animals1();
    }
}

