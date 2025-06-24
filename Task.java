import java.util.*;
import java.time.LocalDate;
public record Task(String name, int dueDate, double worth) {
    public static List<String> splitFixedLength(String str, int chunkSize) {
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < str.length(); i += chunkSize) {
            int end = Math.min(str.length(), i + chunkSize);
            parts.add(str.substring(i, end));
        }
        return parts;
    }
    public LocalDate getDueLocalDate() {
        String dateStr = String.format("%06d", dueDate);
        int day = Integer.parseInt(dateStr.substring(0, 2));
        int month = Integer.parseInt(dateStr.substring(2, 4));
        int year = 2000 + Integer.parseInt(dateStr.substring(4, 6));
        return LocalDate.of(year, month, day);
    }

    public String useString() {
        // Pad the date to 6 digits with leading zeros
        String date = String.format("%06d", dueDate);
        List<String> dateFormat = splitFixedLength(date, 2);
        return "Name: " + name + ", Due Date: " + dateFormat.get(0) + "/" + dateFormat.get(1) + "/" + dateFormat.get(2) + ", Worth: " + worth + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
