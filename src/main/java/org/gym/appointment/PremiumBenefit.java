package org.gym.appointment;

public enum PremiumBenefit {
    SAUNA("sauna"),
    SHOWER("shower"),
    BODY_CHECK("body_check");

    private final String premiumBenefit;


    PremiumBenefit(String premiumBenefit) {
        this.premiumBenefit = premiumBenefit;
    }

    public String getPremiumBenefit() {
        return premiumBenefit;
    }

    @Override
    public String toString() {
        return "PremiumBenefit{" +
                "premiumBenefit='" + premiumBenefit + '\'' +
                '}';
    }
}
