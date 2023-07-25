package courage.model.util;

class random {
    final static java.util.Random r = new java.util.Random();
}

public interface Utils {

    static String jsonMessage(String key, String message) {
        return new StringBuilder("{\"").append(key).append("\":\"").append(message).append("\"}").toString();
    }

    static String[] merger(Object[]... data) {
        // get the total length of the merged array
        int length = 0;
        for (Object[] array : data)
            length += array.length;

        // create a new array with the same length
        String[] merged = new String[length];

        int index = 0; // copy the elements from each array to the merged array
        // use System.arraycopy() instead of for loop
        for (Object[] array : data) {
            System.arraycopy(array, 0, merged, index, array.length);
            index += array.length;
        }
        // return the merged array
        return merged;
    }

    static String generalCode(String firstText, int length) {
        StringBuilder b = new StringBuilder(firstText == null ? "" : firstText);
        int i; // @formatter:off
        while(b.length()<length)
            if(((i=random.r.nextInt(122))>47&&i<58)||(i>64&&i<91)||(i>96&&i<123))
                b.append((char)i);
        return b.length()>length?b.substring(0,length):b.toString();
    }// @formatter:on
}
