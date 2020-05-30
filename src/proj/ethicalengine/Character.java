package proj.ethicalengine;

/**
 * @description: Character
 * @author: Fan Jia
 */
public abstract class Character {
    private Gender gender;
    private BodyType bodyType;
    private int age;
    public static final int DEFAULT_AGE=10;


    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN;
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
        AVERAGE,
        ATHLETIC,
        OVERWEIGHT,
        UNSPECIFIED;
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
        this.age = 1;
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

    public void setAge(int age) {
        this.age = age;
    }


}
