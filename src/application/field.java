package application;

import java.util.Stack;

class field {

    fieldColor color;
    int number;
    int totalColoured;
    int totalEmpty;
    int fieldsNorth, fieldsEast, fieldsSouth, fieldsWest;
    String colouredBy = "";
    Stack<String> alreadyTestedMoves = new Stack<String>();


    field(String encodedField){

        if (encodedField.charAt(0) == 'N' || encodedField.charAt(0) == 'n'){
            color = fieldColor.empty;
            number = 99;
        } else if (encodedField.charAt(0) == 'B' || encodedField.charAt(0) == 'b'){
            color = fieldColor.black;
            number = Character.getNumericValue(encodedField.charAt(1));
        } else if (encodedField.charAt(0) == 'W' || encodedField.charAt(0) == 'w'){
            color = fieldColor.white;
            number = Character.getNumericValue(encodedField.charAt(1));
        } else {
            throw new IllegalArgumentException("Fehlerhaftes Feld im JSON erkannt");
        }
    }

    field(int number, fieldColor color){
        this.number = number;
        this.color = color;
    }

    fieldColor getColor() {
        return color;
    }

    void setColor(fieldColor color) {
        this.color = color;
    }

    int getNumber() {
        return number;
    }

    public String getColouredBy() {
        return colouredBy;
    }

    public void setColouredBy(String colouredBy) {
        this.colouredBy = colouredBy;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFieldsNorth() {
        return fieldsNorth;
    }

    public void setFieldsNorth(int fieldsNorth) {
        this.fieldsNorth = fieldsNorth;
    }

    public int getFieldsEast() {
        return fieldsEast;
    }

    public void setFieldsEast(int fieldsEast) {
        this.fieldsEast = fieldsEast;
    }

    public int getFieldsSouth() {
        return fieldsSouth;
    }

    public void setFieldsSouth(int fieldsSouth) {
        this.fieldsSouth = fieldsSouth;
    }

    public int getFieldsWest() {
        return fieldsWest;
    }

    public void setFieldsWest(int fieldsWest) {
        this.fieldsWest = fieldsWest;
    }

    public int getTotalColoured() {
        return totalColoured;
    }

    public void setTotalColoured(int totalColoured) {
        this.totalColoured = totalColoured;
    }

    public Stack<String> getAlreadyTestedMoves() {
        return alreadyTestedMoves;
    }

    public void setAlreadyTestedMoves(Stack<String> alreadyTestedMoves) {
        this.alreadyTestedMoves = alreadyTestedMoves;
    }

    public int getTotalEmpty() {
        return totalEmpty;
    }

    public void setTotalEmpty(int totalEmpty) {
        this.totalEmpty = totalEmpty;
    }

    @Override
    public String toString() {
        return "field{" +
                "color=" + color +
                ", number=" + number +
                ", totalColoured=" + totalColoured +
                ", totalEmpty=" + totalEmpty +
                ", fieldsNorth=" + fieldsNorth +
                ", fieldsEast=" + fieldsEast +
                ", fieldsSouth=" + fieldsSouth +
                ", fieldsWest=" + fieldsWest +
                ", colouredBy='" + colouredBy + '\'' +
                ", alreadyTestedMoves=" + alreadyTestedMoves +
                '}';
    }
}
