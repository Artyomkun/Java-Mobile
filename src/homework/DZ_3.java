package homework;

public class DZ_3 {

    // Родительский класс Животное
    static abstract class Animal {
        private String name;
        private static int animalCount = 0;

        public Animal(String name) {
            this.name = name;
            animalCount++;
        }

        public String getName() {
            return name;
        }

        public static int getAnimalCount() {
            return animalCount;
        }

        public abstract void run(int distance);

        public abstract void swim(int distance);
    }

    // Класс Кот
    static class Cat extends Animal {
        private static final int MAX_RUN_DISTANCE = 200;
        private static int catCount = 0;

        public Cat(String name) {
            super(name);
            catCount++;
        }

        public static int getCatCount() {
            return catCount;
        }

        @Override
        public void run(int distance) {
            if (distance <= 0) {
                System.out.println(getName() + " не может пробежать " + distance + " м.");
                return;
            }

            if (distance <= MAX_RUN_DISTANCE) {
                System.out.println(getName() + " пробежал " + distance + " м.");
            } else {
                System.out.println(getName() + " не смог пробежать " + distance + " м. (максимум " + MAX_RUN_DISTANCE + " м.)");
            }
        }

        @Override
        public void swim(int distance) {
            // Кот не умеет плавать
            System.out.println(getName() + " не умеет плавать.");
        }
    }

    // Класс Собака
    static class Dog extends Animal {
        private static final int MAX_RUN_DISTANCE = 500;
        private static final int MAX_SWIM_DISTANCE = 10;
        private static int dogCount = 0;

        public Dog(String name) {
            super(name);
            dogCount++;
        }

        public static int getDogCount() {
            return dogCount;
        }

        @Override
        public void run(int distance) {
            if (distance <= 0) {
                System.out.println(getName() + " не может пробежать " + distance + " м.");
                return;
            }

            if (distance <= MAX_RUN_DISTANCE) {
                System.out.println(getName() + " пробежал " + distance + " м.");
            } else {
                System.out.println(getName() + " не смог пробежать " + distance + " м. (максимум " + MAX_RUN_DISTANCE + " м.)");
            }
        }

        @Override
        public void swim(int distance) {
            if (distance <= 0) {
                System.out.println(getName() + " не может проплыть " + distance + " м.");
                return;
            }

            if (distance <= MAX_SWIM_DISTANCE) {
                System.out.println(getName() + " проплыл " + distance + " м.");
            } else {
                System.out.println(getName() + " не смог проплыть " + distance + " м. (максимум " + MAX_SWIM_DISTANCE + " м.)");
            }
        }
    }

    public static void main(String[] args) {
        // Создаем массив из 7 животных
        Animal[] animals = {
                new Cat("Мурзик"),
                new Dog("Бобик"),
                new Cat("Барсик"),
                new Dog("Шарик"),
                new Cat("Том"),
                new Dog("Рекс"),
                new Cat("Васька")
        };

        System.out.println("=== Проверка плавания ===");
        // С помощью цикла foreach вызываем метод плавать только у тех животных, кто умеет плавать
        for (Animal animal : animals) {
            if (animal instanceof Dog) {
                animal.swim(5);
            }
        }

        System.out.println("\n=== Подсчет ===");
        System.out.println("Создано котов: " + Cat.getCatCount());
        System.out.println("Создано собак: " + Dog.getDogCount());
        System.out.println("Создано животных всего: " + Animal.getAnimalCount());
    }
}