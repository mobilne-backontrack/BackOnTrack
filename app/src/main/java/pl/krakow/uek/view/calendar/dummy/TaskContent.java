package pl.krakow.uek.view.calendar.dummy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskContent {

    public static final List<TaskItem> ITEMS = new ArrayList<>();
    public static final Map<Integer, TaskItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createItem(i));
        }
    }

    private static void addItem(TaskItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static TaskItem createItem(int position) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return new TaskItem(1, "Umyć psa", false, dateFormat.format(new Date()), Collections.singletonList("tag"), true);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore address information here.");
        }
        return builder.toString();
    }

    public static class TaskItem {
        private int id;
        private String name;
        private List<String> tag;
        private boolean finished;
        private String date;
        private boolean notification;

        public TaskItem() {

        }

        public TaskItem(int id, String name, boolean finished, String date, List<String> tag, boolean notification) {
            this.id = id;
            this.name = name;
            this.finished = finished;
            this.date = date;
            this.tag = tag;
            this.notification = notification;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getTag() {
            return tag;
        }

        public boolean getFinished() {
            return finished;
        }

        public String getDate() {
            return date;
        }

        public boolean getNotification() {
            return notification;
        }
    }
}
