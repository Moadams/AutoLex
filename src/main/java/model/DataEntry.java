package main.java.model;

import java.util.Objects;

public class DataEntry {
    private String id;
    private String name;
    private String position;

    public DataEntry(String id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataEntry)) return false;
        DataEntry entry = (DataEntry) o;
        return id.equals(entry.id);  // Unique by ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " - " + position;
    }
}
