package com.keerthi.wouldyourather;

public class Question {

    private String optionA;
    private String imageA;
    private String typeA;

    private String optionB;
    private String imageB;
    private String typeB;

    public Question(String optionA, String imageA, String typeA,
                    String optionB, String imageB, String typeB) {
        this.optionA = optionA;
        this.imageA = imageA;
        this.typeA = typeA;
        this.optionB = optionB;
        this.imageB = imageB;
        this.typeB = typeB;
    }

    public String getOptionA() { return optionA; }
    public String getImageA() { return imageA; }
    public String getTypeA() { return typeA; }

    public String getOptionB() { return optionB; }
    public String getImageB() { return imageB; }
    public String getTypeB() { return typeB; }
}