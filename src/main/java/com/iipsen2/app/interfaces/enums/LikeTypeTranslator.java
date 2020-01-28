package com.iipsen2.app.interfaces.enums;

/**
 * Translates the tinyint value from the database to the LikeType enum value
 *
 * @author Mazeyar Rezaei
 * @since 05-11-2019
 * @deprecated database attribute should be changed to string,
 * because system should not have to translate the value because its an enum value
 */
public class LikeTypeTranslator {
    public static LikeType translate(int type) {
        switch (type) {
            case 1:
                return LikeType.LIKE;
            case 2:
                return LikeType.DISLIKE;
            case 3:
                return LikeType.UP_VOTE;
            case 4:
                return LikeType.DOWN_VOTE;
            default:
                return LikeType.NEUTRAL;
        }
    }
}
