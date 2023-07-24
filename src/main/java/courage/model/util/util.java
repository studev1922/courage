package courage.model.util;

public interface util {

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

    static String jsonMessage(String key, String message) {
        return new StringBuilder("{\"").append(key).append("\":\"").append(message).append("\"}").toString();
    }
}
