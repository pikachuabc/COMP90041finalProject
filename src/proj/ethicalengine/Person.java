package proj.ethicalengine;

/**
 * @description:Person
 * @author: Fan Jia
 */
public class Person extends Character {
    private boolean isPregnant;
    private Profession profession;
    private boolean isYou = false;



    public enum Profession {
        DOCTOR,
        CEO,
        CRIMINAL,
        HOMELESS,
        UNEMPLOYED,
        UNKNOWN,
        PROGRAMMER,
        TEACHER,
        NONE;
        public static Profession contains(String type) throws InvalidCharacteristicException {
            for (Profession profession : Profession.values()) {
                if (profession.name().equals(type.toUpperCase())) {
                    return profession;
                }
            }
            throw new InvalidCharacteristicException("WARNING: invalid characteristic in config file in line ");
        }
    }

    public enum AgeCategory {
        BABY,
        CHILD,
        ADULT,
        SENIOR;
    }

    public Person(Gender gender, BodyType bodyType, int age, boolean isPregnant, Profession profession) {
        super(gender, bodyType, age);
        this.isPregnant = isPregnant;
        this.profession = profession;
    }

    public Person(Person otherPerson) {
        this.isPregnant = otherPerson.isPregnant();
        this.profession = otherPerson.getProfession();
        this.setAge(otherPerson.getAge());
        this.setBodyType(otherPerson.getBodyType());
        this.setGender(otherPerson.getGender());
    }

    public boolean isPregnant() {
        return isPregnant;
    }

    public void setPregnant(boolean pregnant) {
        this.isPregnant = pregnant;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public AgeCategory getAgeCategory() {
        int age = getAge();
        if (age <= 5 && age >= 0) {
            return AgeCategory.BABY;
        } else if (age <= 16 && age >= 5) {
            return AgeCategory.CHILD;
        } else if (age <= 68 && age >= 17) {
            return AgeCategory.ADULT;
        } else {
            return AgeCategory.SENIOR;
        }
    }

    public boolean isYou() {
        return isYou;
    }

    public void setAsYou(boolean isYou) {
        this.isYou = isYou;
    }



    @Override
    public String toString() {

        String display = "";
        if (isYou()) {
            display = display + "you ";
        }
        display+= getBodyType().toString().toLowerCase()+" ";
        display = display + getAgeCategory().toString().toLowerCase() + " ";
        if (getAgeCategory().equals(AgeCategory.ADULT)) {
            display = display + getProfession().toString().toLowerCase() + " ";
        }
        display = display + getGender().toString().toLowerCase() + " ";
        if (isPregnant()) {
            display = display + "pregnant";
        }
        return display;
    }
}
