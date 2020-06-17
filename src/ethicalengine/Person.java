package ethicalengine;

/**
 * @description: Person
 * @author: Fan Jia
 */
public class Person extends Character {
    private boolean isPregnant;
    private Profession profession;
    private boolean isYou = false;

    public enum Profession {
        DOCTOR(1),
        CEO(1),
        CRIMINAL(1),
        HOMELESS(1),
        UNEMPLOYED(1),
        UNKNOWN(1),
        PROGRAMMER(1),
        TEACHER(1),
        NONE(1);
        private final double coefficient;

        Profession(double coefficient) {
            this.coefficient = coefficient;
        }

        public double getCoefficient() {
            return coefficient;
        }
        public static Profession contains(String type) throws InvalidCharacteristicException {
            for (Profession profession : Profession.values()) {
                if (profession.name().equals(type.toUpperCase())) {
                    return profession;
                }
            }
            throw new InvalidCharacteristicException();
        }
    }

    public enum AgeCategory {
        BABY(1),
        CHILD(1),
        ADULT(1),
        SENIOR(1);

        private final double coefficient;

        AgeCategory(double coefficient) {
            this.coefficient = coefficient;
        }

        public double getCoefficient() {
            return coefficient;
        }
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

    /**
     * Gain person's additional mark based on their profession,pregnancy state,
     * if this person is usr
     *
     * @author Fan Jia
     * @methodName getMark
     * @return double
     */
    @Override
    public double getMark() {
        double mark = super.getMark();
        mark += this.profession.getCoefficient();
        if (isPregnant) {
            mark += 5;
        }
        if (isYou) {
            mark+= 1000000; //I WILL ALIVE!!!!
        }
        return mark;
    }


    @Override
    public String toString() {

        String display = "";
        if (isYou()) {
            display = display + "you ";
        }
        display+= getBodyType().toString().toLowerCase()+" ";                       //body type
        display+= getAgeCategory().toString().toLowerCase() + " ";        //age category
        if (getAgeCategory().equals(AgeCategory.ADULT)) {
            display = display + getProfession().toString().toLowerCase() + " ";     //profession
        }
        display = display + getGender().toString().toLowerCase() + " ";             //gender
        if (isPregnant()) {
            display = display + "pregnant";                                         //pregnant
        }
        return display;
    }
}
