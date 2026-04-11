package nl.fontys.newswebapplication.domain.enums;

public enum Genre {
    BREAKING_NEWS,
    POLITICS,
    BUSINESS_ECONOMICS,
    GOVERNMENT,
    SCIENCE,
    TECHNOLOGY,
    ART,
    HEALTH_WELLNESS,
    ENTERTAINMENT,
    SPORT,
    TRAVEL;

    public static boolean exists(int id) {
        for (Genre genre : Genre.values()) {
            if (genre.ordinal() == id ) {
                return true;
            }
        }
        return false;
    }
}

