package com.ferra13671.BTbot.api.Utils.Generate;

public class StringGenerator {

    private static final int[] lowerCaseWords = {97, 122};
    private static final int[] capsWords = {65, 90};
    private static final int[] otherSymbols = {33, 46};
    private static final int[] numbers = {48, 57};


    public static String generateNextString(int length, boolean andCaps, boolean andNumbers, boolean andOtherSymbols) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length + 1; i++) {
            char nextSymbol = (char) 0;
            while (nextSymbol == (char) 0) {
                switch (NumberGenerator.generateInt(1,4)) {
                    case 1:
                        nextSymbol = (char) NumberGenerator.generateInt(lowerCaseWords[0], lowerCaseWords[1]);
                        break;
                    case 2:
                        if (andCaps) {
                            nextSymbol = (char) NumberGenerator.generateInt(capsWords[0], capsWords[1]);
                        }
                        break;
                    case 3:
                        if (andNumbers) {
                            nextSymbol = (char) NumberGenerator.generateInt(numbers[0], numbers[1]);
                        }
                        break;
                    case 4:
                        if (andOtherSymbols) {
                            nextSymbol = (char) NumberGenerator.generateInt(otherSymbols[0], otherSymbols[1]);
                        }
                        break;
                }
            }
            stringBuilder.append(nextSymbol);
        }
        return stringBuilder.toString();
    }
}
