package ethicalengine;

import java.util.ArrayList;

/**
 * @description: Character
 * @author: Fan Jia
 */
public abstract class Character {

    public static class InvalidCharacteristicException extends Exception {
        public InvalidCharacteristicException(int lineNumber) {
            super("WARNING: invalid data format in config file in line "+lineNumber);
        }
        public InvalidCharacteristicException(String message) {
            super(message);
        }
    }

    private Gender gender;
    private BodyType bodyType;
    private int age;
    public static final int DEFAULT_AGE=10;



    public enum Gender {
        MALE(1),
        FEMALE(1),
        UNKNOWN(1);

        private final double coefficient;

        Gender(double coefficient) {
            this.coefficient = coefficient;
        }

        public double getCoefficient() {
            return coefficient;
        }

        public static Gender contains(String type) throws InvalidCharacteristicException {
            for (Gender gender : Gender.values()) {
                if (gender.name().equals(type.toUpperCase())) {
                    return gender;
                }
            }
            throw new InvalidCharacteristicException("WARNING: invalid characteristic in config file in line ");
        }
    }

    public enum BodyType {
        AVERAGE(1),
        ATHLETIC(1),
        OVERWEIGHT(1),
        UNSPECIFIED(1);

        private final double coefficient;

        BodyType(double coefficient) {
            this.coefficient = coefficient;
        }

        public double getCoefficient() {
            return coefficient;
        }
        public static BodyType contains(String type) throws InvalidCharacteristicException {
            for (BodyType bodyType : BodyType.values()) {
                if (bodyType.name().equals(type.toUpperCase())) {
                    return bodyType;
                }
            }
            throw new InvalidCharacteristicException("WARNING: invalid characteristic in config file in line ");
        }
    }

    public Character() {
        this.gender = Gender.UNKNOWN;
        this.bodyType = BodyType.UNSPECIFIED;
        this.age = DEFAULT_AGE;
    }

    public Character(Character c) {
        this.gender= c.getGender();
        this.age = c.getAge();
        this.bodyType = c.getBodyType();
    }

    public Character(Gender gender, BodyType bodyType, int age) {
        this.gender = gender;
        this.bodyType = bodyType;
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age){
        if (age <= 0) {
            System.out.println("invalid age value, check it");
            return;
        }
        this.age = age;
    }

    /**
     * each character has a basic mark depending on their body type and gender,
     * for specific categories, person and animal have their additional mark
     *
     * @author Fan Jia
     * @methodName getMark
     * @return double
     * @see Person#getMark()
     * @see Animal#getMark()
     */
    public double getMark() {
        return  getGender().getCoefficient()+
                getBodyType().getCoefficient();
    }




}
