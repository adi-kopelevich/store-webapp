package sample.task.list.service;

import java.util.List;

public class TaskItem {

    private int id;
    private String name;
    private String category;
    private long reminder;
    private List<String> notes;

    public TaskItem() { // for serialization
    }

    public TaskItem(int id, String name, String category, long reminder, List<String> notes) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.reminder = reminder;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getReminder() {
        return reminder;
    }

    public void setReminder(long reminder) {
        this.reminder = reminder;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public String toString() {
        return new StringBuilder().append("{")
                .append("id: ").append(id).append(", ")
                .append("name: ").append(name).append(", ")
                .append("category: ").append(category).append(", ")
                .append("reminder: ").append(reminder).append(", ")
                .append("notes: ").append(notes).append("}")
                .toString();
    }

    // mainly for comparison within a collection
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskItem taskItem = (TaskItem) o;

        if (id != taskItem.id) return false;
        if (reminder != taskItem.reminder) return false;
        if (!category.equals(taskItem.category)) return false;
        if (!name.equals(taskItem.name)) return false;
        if (!notes.equals(taskItem.notes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + (int) (reminder ^ (reminder >>> 32));
        result = 31 * result + notes.hashCode();
        return result;
    }
}
